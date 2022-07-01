package com.group7.clientsservice.repository;

import com.group7.clientsservice.model.Client;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ClientRepository extends ReactiveMongoRepository<Client, String> {
    Mono<Client> findClientByDocumentTypeAndDocumentNumber(String documentType, String documentNumber);
}
