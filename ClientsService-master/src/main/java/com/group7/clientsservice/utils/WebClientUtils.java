package com.group7.clientsservice.utils;

import com.group7.clientsservice.exception.accounts.AccountsNotFoundException;
import com.group7.clientsservice.exception.accounts.CreditsNotFoundException;
import com.group7.clientsservice.model.Accounts;
import com.group7.clientsservice.model.Credit;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class WebClientUtils {

    @Value("${services-uri.accounts}")
    private String accountsService;

    @Value("${services-uri.credits}")
    private String creditsService;

    @CircuitBreaker(name = "accounts",fallbackMethod = "accountsUnavailable")
    public Flux<Accounts> getAccounts(String id) {
        return WebClient.create()
                .mutate()
                .baseUrl(accountsService + "/client")
                .build()
                .get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, errorResponse -> Mono.error(new AccountsNotFoundException("Not found Accounts with Client ID: " + id)))
                .bodyToFlux(Accounts.class);
    }

    public Flux<String> accountsUnavailable(String id, Exception ex) {
        return Flux.just("Accounts service unavailable, ID: "+ id + ", Exception: " + ex.getMessage());
    }

    @CircuitBreaker(name = "credits",fallbackMethod = "creditsUnavailable")
    public Flux<Credit> getCredits(String id) {
        return WebClient.create()
                .mutate()
                .baseUrl(creditsService+"/credit_cards/client")
                .build()
                .get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, errorResponse -> Mono.error(new CreditsNotFoundException("Not found Credits with Client ID: " + id)))
                .bodyToFlux(Credit.class);
    }

    public Flux<String> creditsUnavailable(String id, Exception ex) {
        return Flux.just("Credits service unavailable, ID: "+ id + ", Exception: " + ex.getMessage());
    }

}
