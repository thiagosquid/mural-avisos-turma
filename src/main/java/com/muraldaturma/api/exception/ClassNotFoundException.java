package com.muraldaturma.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClassNotFoundException extends RuntimeException {

    public ClassNotFoundException(String description, String causeMessage) {
        super(description, new Throwable(causeMessage, null));
    }

}
