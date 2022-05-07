package com.muraldaturma.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UsernameAlreadyExistsException extends RuntimeException{

    public UsernameAlreadyExistsException(String description, String causeMessage) {
        super(description, new Throwable(causeMessage,null));
    }
}
