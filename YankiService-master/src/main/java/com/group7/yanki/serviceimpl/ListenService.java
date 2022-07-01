package com.group7.yanki.serviceimpl;

import com.group7.yanki.dto.Result;
import com.group7.yanki.model.Account;
import com.group7.yanki.model.Yanki;
import com.group7.yanki.repository.AccountRepository;
import com.group7.yanki.repository.YankiRepository;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Consumer;

@Service
@Slf4j
public class ListenService {

    private RMapReactive<Long, Account> accountMap;

    @Autowired
    private YankiRepository yankiRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MessageServiceImpl messageService;

    private static final String SUCCESS = "Success";
    private static final String FAILED = "Failed";

    public ListenService(RedissonReactiveClient client) {
        this.accountMap = client.getMap("account", new TypedJsonJacksonCodec(Long.class, Account.class));
    }

    @Bean
    Consumer<Yanki> toyanki() {
        return yanki -> accountMap.get(yanki.getTo())
                .flatMap(accountTo -> {
                    //Si tiene cuenta (To)
                    if (!Objects.isNull(accountTo.getDebitCard())) {
                        return yankiRepository.save(yanki);
                    }

                    //Si no tiene cuenta (To)
                    accountTo.setBalance(accountTo.getBalance() + yanki.getAmount());
                    return accountMap.fastPut(accountTo.getPhone(), accountTo)
                            .thenReturn(accountTo)
                            .flatMap(account -> accountRepository.save(accountTo))
                            .then(Mono.just(messageService.sendResult(Result.builder()
                                    .to(accountTo.getPhone())
                                    .status(SUCCESS)
                                    .message("You received a Yanki of " + yanki.getAmount() + " from " + yanki.getFrom())
                                    .build())))
                            .then(yankiRepository.save(yanki));
                })
                .doOnSuccess(x -> log.info("Account from: {}", x))
                .subscribe();
    }

    @Bean
    Consumer<Yanki> bootcointoyanki() {
        return yanki -> accountMap.get(yanki.getFrom())
                .flatMap(accountFrom -> {
                    Double amountFrom = (yanki.getQuantity()*yanki.getUnitPrice());
                    Double amountTo = amountFrom - (amountFrom * yanki.getSellRate());

                    //Si no tiene cuenta (To)
                    accountFrom.setBalance(accountFrom.getBalance() - amountFrom);
                    return accountMap.fastPut(accountFrom.getPhone(), accountFrom)
                            .thenReturn(accountFrom)
                            .flatMap(account -> accountRepository.save(accountFrom))
                            .then(Mono.just(messageService.sendResult(Result.builder()
                                    .to(accountFrom.getPhone())
                                    .status(SUCCESS)
                                    .message("You make a Yanki of " + amountFrom + " to " + yanki.getTo())
                                    .build())))
                            .flatMap(p -> {
                                return accountMap.get(yanki.getTo())
                                        .flatMap(accountTo -> {
                                            accountTo.setBalance(accountTo.getBalance() + amountTo);
                                            return accountMap.fastPut(accountTo.getPhone(), accountTo)
                                                    .thenReturn(accountTo)
                                                    .flatMap(account -> accountRepository.save(accountTo))
                                                    .then(Mono.just(messageService.sendResult(Result.builder()
                                                            .to(accountTo.getPhone())
                                                            .status(SUCCESS)
                                                            .message("You received a Yanki of " + amountTo + " from " + yanki.getFrom())
                                                            .build())));
                                        });
                            })
                            .then(yankiRepository.save(yanki));
                })
                .doOnSuccess(x -> log.info("Account from: {}", x))
                .subscribe();
    }

    /*
    @Bean
    Consumer<LinkRequest> link() {
        return linkRequest -> {
            if (linkRequest.getState().equals("true")) {
                accountMap.get(linkRequest.getPhone())
                        .flatMap(account -> {
                            account.setBalance(0.0);
                            account.setDebitCard(linkRequest.getDebitCard());
                            return accountMap.fastPut(account.getPhone(), account)
                                    .thenReturn(account)
                                    .flatMap(accountAux -> accountRepository.save(accountAux))
                                    .then(Mono.just(messageService.sendResult(Result.builder()
                                            .to(account.getPhone())
                                            .status(SUCCESS)
                                            .message("You linked your card successfully")
                                            .build())));
                        })
                        .subscribe();
            } else if (linkRequest.getState().equals("false")) {
                messageService.sendResult(Result.builder()
                        .to(linkRequest.getPhone())
                        .status(FAILED)
                        .message("There is not debit card with that ID")
                        .build());
            }
        };
    }
     */

}
