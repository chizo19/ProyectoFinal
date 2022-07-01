package com.group7.creditsservice.utils;

import com.group7.creditsservice.model.Client;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
public class WebClientUtils {
    private WebClient webClient;

    @Value("${services-uri.clients}")
    private String clientService;

    public WebClientUtils() {
        this.webClient = WebClient.create(clientService);
    }

    public WebClientUtils(WebClient webClient) {
        this.webClient = webClient;
    }

    @CircuitBreaker(name="clients", fallbackMethod = "clientsUnavailable")
    public Mono<Client> getClient(String id) {
        return webClient
                .mutate()
                .baseUrl(clientService)
                .build()
                .get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Client.class)
                .onErrorResume(WebClientResponseException.class,
                        ex->ex.getRawStatusCode() == 404 ? Mono.empty(): Mono.error(ex));
    }

    public Mono<String> clientsUnavailable(Exception ex) {
        return Mono.error(new Exception("Client service unavailable "+ex.getMessage()));
    }
}
