package com.group7.clientsservice.exception.client;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClientsCreationException extends RuntimeException {

    public ClientsCreationException(String message) {
        super(message);
    }
}

