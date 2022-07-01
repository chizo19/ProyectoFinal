package com.group7.clientsservice.serviceimpl;

import com.group7.clientsservice.dto.ClientsRequestDto;
import com.group7.clientsservice.dto.ClientsResponseDto;
import com.group7.clientsservice.exception.accounts.AccountsNotFoundException;
import com.group7.clientsservice.exception.accounts.CreditsNotFoundException;
import com.group7.clientsservice.exception.client.ClientsCreationException;
import com.group7.clientsservice.exception.client.ClientsDuplicationException;
import com.group7.clientsservice.exception.client.ClientsNotFoundException;
import com.group7.clientsservice.model.Accounts;
import com.group7.clientsservice.model.Client;
import com.group7.clientsservice.model.Credit;
import com.group7.clientsservice.repository.ClientRepository;
import com.group7.clientsservice.utils.WebClientUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ClienteServiceImplTest {


    @InjectMocks
    ClientServiceImpl clienteService;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private WebClientUtils webClientUtils;
    private Client client;
    private ClientsRequestDto clientRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        client = Client.builder()
                .id("123")
                .documentType("DNI")
                .documentNumber("74910877")
                .firstName("Cristian")
                .lastName("Paredes")
                .businessName("")
                .type("personal")
                .profile("vip")
                .build();

        clientRequestDto = ClientsRequestDto.builder()
                .documentType("DNI")
                .documentNumber("74910877")
                .firstName("Cristian")
                .lastName("Paredes")
                .businessName("")
                .type("personal")
                .profile("vip")
                .build();
    }

    @Test
    void getAll() {
        when(clientRepository.findAll()).thenReturn(Flux.just(client));
        assertNotNull(clienteService.getAll());
    }

    @Test
    void getById() {
        when(clientRepository.findById(any(String.class))).thenReturn(Mono.just(client));
        assertNotNull(clienteService.getById("123"));
    }

    @Test
    void ByDocumentTypeAndDocumentNumber() {
        when(clientRepository.findClientByDocumentTypeAndDocumentNumber(any(String.class), any(String.class)))
                .thenReturn(Mono.just(client));
        assertNotNull(clienteService.getByDocTypeAndDocNumber("DNI", "74910877"));
    }

    @Test
    void delete() {
        when(clientRepository.findById(any(String.class))).thenReturn(Mono.just(client));
        when(clientRepository.delete(any(Client.class))).thenReturn(Mono.empty());
        assertNotNull(clienteService.delete("123"));
    }

    @Test
    void save() {
        when(clientRepository.findClientByDocumentTypeAndDocumentNumber(any(String.class), any(String.class)))
                .thenReturn(Mono.empty());
        when(clientRepository.insert(any(Client.class)))
                .thenReturn(Mono.just(clientRequestDto.toModel()));
        StepVerifier.create(clienteService.save(clientRequestDto))
                .assertNext(client -> {
                    assertNotNull(client);
                })
                .verifyComplete();

    }

    @Test
    void saveClientsCreationException1() {
        when(clientRepository.findClientByDocumentTypeAndDocumentNumber(any(String.class), any(String.class)))
                .thenReturn(Mono.empty());
        when(clientRepository.insert(any(Client.class)))
                .thenThrow(new ClientsNotFoundException("FirstName and LastName are required"));

        clientRequestDto.setFirstName(null);
        clientRequestDto.setLastName(null);

        StepVerifier.create(clienteService.save(clientRequestDto))
                .verifyError(ClientsCreationException.class);

    }

    @Test
    void saveClientsCreationException2() {
        when(clientRepository.findClientByDocumentTypeAndDocumentNumber(any(String.class), any(String.class)))
                .thenReturn(Mono.empty());
        when(clientRepository.insert(any(Client.class)))
                .thenThrow(new ClientsCreationException("FirstName and LastName are required"));

        clientRequestDto.setFirstName(null);
        clientRequestDto.setLastName(null);

        StepVerifier.create(clienteService.save(clientRequestDto))
                .verifyError(ClientsCreationException.class);

    }

    @Test
    void saveClientsDuplicationException() {
        when(clientRepository.findClientByDocumentTypeAndDocumentNumber(any(String.class), any(String.class)))
                .thenReturn(Mono.empty());
        when(clientRepository.insert(any(Client.class))).thenThrow(new ClientsDuplicationException(""));
        StepVerifier.create(clienteService.save(clientRequestDto))
                .verifyError(ClientsDuplicationException.class);
    }

    @Test
    void saveClientsCreationException() {
        when(clientRepository.findClientByDocumentTypeAndDocumentNumber(any(String.class), any(String.class)))
                .thenReturn(Mono.empty());
        when(clientRepository.insert(any(Client.class))).thenThrow(new ClientsCreationException(""));
        StepVerifier.create(clienteService.save(clientRequestDto))
                .verifyError(ClientsCreationException.class);
    }

    @Test
    void update() {
        when(clientRepository.findById(any(String.class))).thenReturn(Mono.just(client));
        when(clientRepository.save(any(Client.class))).thenReturn(Mono.just(clientRequestDto.toModel()));
        StepVerifier.create(clienteService.update("123", clientRequestDto))
                .assertNext(client -> {
                    assertNotNull(client);
                })
                .verifyComplete();
    }

    @Test
    void updateClientsCreationException() {
        when(clientRepository.findById(any(String.class))).thenReturn(Mono.just(client));
        when(clientRepository.save(any(Client.class))).thenThrow(new ClientsCreationException(""));
        StepVerifier.create(clienteService.update("123", clientRequestDto))
                .verifyError(ClientsCreationException.class);
    }

    @Test
    void existsById() {
        when(clientRepository.existsById(any(String.class))).thenReturn(Mono.just(true));
        assertNotNull(clienteService.existsById(client.getId()));
    }

    @Test
    void getProductsByClient() {

        Accounts accounts =  new Accounts();
        accounts.setId("123");
        accounts.setClient("456");
        accounts.setType("personal");
        accounts.setClientProfile("vip");

        Credit credit = new Credit();
        credit.setId("123");
        credit.setClient("456");
        credit.setAmount(50);

        when(clientRepository.findById(any(String.class))).thenReturn(Mono.just(client));
        when(webClientUtils.getAccounts(any(String.class))).thenReturn(Flux.just(accounts));
        when(webClientUtils.getCredits(any(String.class))).thenReturn(Flux.just(credit));

        StepVerifier.create(clienteService.getProductsByClient("123"))
                .assertNext(products -> {
                            assertNotNull(products);
                        })
                .verifyComplete();

    }

    @Test
    void getProductsByClientAccountsNotFoundException() {
        when(clientRepository.findById(any(String.class))).thenReturn(Mono.just(client));
        when(webClientUtils.getAccounts(any(String.class))).thenThrow(new AccountsNotFoundException(""));

        StepVerifier.create(clienteService.getProductsByClient("123"))
                .verifyError(AccountsNotFoundException.class);

    }

    @Test
    void getProductsByClientCreditsNotFoundException() {

        Accounts accounts =  new Accounts();
        accounts.setId("123");
        accounts.setClient("456");
        accounts.setType("personal");
        accounts.setClientProfile("vip");

        when(clientRepository.findById(any(String.class))).thenReturn(Mono.just(client));
        when(webClientUtils.getAccounts(any(String.class))).thenReturn(Flux.just(accounts));
        when(webClientUtils.getCredits(any(String.class))).thenThrow(new CreditsNotFoundException(""));

        StepVerifier.create(clienteService.getProductsByClient("123"))
                .verifyError(CreditsNotFoundException.class);

    }
}