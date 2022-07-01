package com.group7.clientsservice.exception.accounts;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CreditsNotFoundException extends RuntimeException {

    public CreditsNotFoundException(String message) {
        super(message);
    }

}
