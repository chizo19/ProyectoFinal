package com.group7.clientsservice.exception.client;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ClientsDuplicationException extends RuntimeException {

    public ClientsDuplicationException(String message) {
        super(message);
    }
}
