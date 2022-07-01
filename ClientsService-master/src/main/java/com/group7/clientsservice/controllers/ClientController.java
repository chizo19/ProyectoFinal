package com.group7.clientsservice.controllers;

import com.group7.clientsservice.dto.ClientsRequestDto;
import com.group7.clientsservice.dto.ClientsResponseDto;
import com.group7.clientsservice.dto.ProductsResponseDto;
import com.group7.clientsservice.model.Client;
import com.group7.clientsservice.service.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private IClientService iClientService;

    @GetMapping
    public Flux<Client> getClientAll() {
        return iClientService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<Client> getClientById(@PathVariable String id) {
        return iClientService.getById(id);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteClient(@PathVariable String id) {
        return iClientService.delete(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ClientsResponseDto> saveCliente(@RequestBody ClientsRequestDto clientsDto) {
        return iClientService.save(clientsDto);
    }

    @PutMapping("/{id}")
    public Mono<ClientsResponseDto> updateCliente(@PathVariable String id, @RequestBody ClientsRequestDto cliente) {
        return iClientService.update(id, cliente);
    }

    @GetMapping("/{id}/products")
    public Mono<ProductsResponseDto> getProductsByClient(@PathVariable String id) {
        return iClientService.getProductsByClient(id);
    }
}
