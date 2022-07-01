package com.group7.clientsservice.serviceimpl;

import com.group7.clientsservice.dto.ClientsRequestDto;
import com.group7.clientsservice.dto.ClientsResponseDto;
import com.group7.clientsservice.dto.ProductsResponseDto;
import com.group7.clientsservice.exception.client.ClientsDuplicationException;
import com.group7.clientsservice.exception.client.ClientsNotFoundException;
import com.group7.clientsservice.model.Client;
import com.group7.clientsservice.repository.ClientRepository;
import com.group7.clientsservice.service.IClientService;
import com.group7.clientsservice.utils.WebClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ClientServiceImpl implements IClientService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private WebClientUtils webClientUtils;

    @Override
    public Flux<Client> getAll() {
        return clientRepository.findAll().doOnComplete(() -> log.info("Retrieving all Clients"));
    }

    @Override

    public Mono<Client> getById(String id) {
        return clientRepository.findById(id)
                .switchIfEmpty(Mono.error(new ClientsNotFoundException("Clients not found with id: " + id)))
                .doOnError(ex -> log.error("Error find client", ex));
    }

    public Mono<Client> getByDocTypeAndDocNumber(String documentType, String documentNumber) {
        return clientRepository.findClientByDocumentTypeAndDocumentNumber(documentType, documentNumber);
    }

    @Override
    public Mono<Void> delete(String id) {
        return getById(id)
                .flatMap(c -> clientRepository.delete(c));
    }

    @Override
    public Mono<ClientsResponseDto> save(ClientsRequestDto clientsRequestDto) {
        return getByDocTypeAndDocNumber(clientsRequestDto.getDocumentType(), clientsRequestDto.getDocumentNumber())
                .hasElement()
                .flatMap(exists -> {
                    if (exists) return Mono.error(new ClientsDuplicationException("Client already exists by " +
                            "DocumentType: "+clientsRequestDto.getDocumentType()+", DocumentNumber: "+ clientsRequestDto.getDocumentNumber()));
                    return clientRepository.insert(clientsRequestDto.toModel());
                })
                .map(ClientsResponseDto::fromModel)
                .doOnSuccess(c -> log.info("Created new client with ID: {}", c.getId()))
                .doOnError(ex -> log.error("Error creating new client ", ex));
    }

    @Override
    public Mono<ClientsResponseDto> update(String id, ClientsRequestDto clienteDto) {
        return getById(id)
                .flatMap(clientBD -> {
                    Client clientNew = clienteDto.toModel();
                    clientNew.setId(clientBD.getId());
                    clientNew.setDocumentType(clientBD.getDocumentType());
                    clientNew.setDocumentNumber(clientBD.getDocumentNumber());
                    return clientRepository.save(clientNew);
                })
                .map(ClientsResponseDto::fromModel)
                .doOnSuccess(ex -> log.info("Update Client with id: {}", id))
                .doOnError(ex -> log.error("Error update Client ", ex));
    }

    @Override
    public Mono<Boolean> existsById(String id) {
        return clientRepository.existsById(id);
    }

    public Mono<ProductsResponseDto> getProductsByClient(String id) {
        return getById(id)
                .flatMap(client -> {
                    ProductsResponseDto clientProducts = new ProductsResponseDto(client);
                    return webClientUtils.getAccounts(id)
                            .collectList()
                            .flatMap(acc -> {
                                clientProducts.setAccounts(acc);
                                return webClientUtils.getCredits(id)
                                        .collectList()
                                        .flatMap(cred -> {
                                            clientProducts.setCredit(cred);
                                            return Mono.just(clientProducts);
                                        });
                            })
                            .doOnError(ex -> log.error("Error find Products by Client ", ex));
                });
    }

}
