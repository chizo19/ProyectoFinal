package com.group7.clientsservice.exception.client;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClientsNotFoundException extends RuntimeException {

    public ClientsNotFoundException(String message) {
        super(message);
    }

}
