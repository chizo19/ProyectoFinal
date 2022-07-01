package com.group7.clientsservice.service;

import com.group7.clientsservice.dto.ClientsRequestDto;
import com.group7.clientsservice.dto.ClientsResponseDto;
import com.group7.clientsservice.dto.ProductsResponseDto;
import com.group7.clientsservice.model.Client;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IClientService {
    Flux<Client> getAll();

    Mono<Client> getById(String id);

    Mono<Void> delete(String id);

    Mono<ClientsResponseDto> save(ClientsRequestDto clientsDto);

    Mono<ClientsResponseDto> update(String id, ClientsRequestDto cliente);

    Mono<Boolean> existsById(String id);

    Mono<ProductsResponseDto> getProductsByClient(String id);
}
