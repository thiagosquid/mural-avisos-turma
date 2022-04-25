package com.muraldaturma.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TagException extends RuntimeException {
    public TagException(String description, String causeMessage) {
        super(description, new Throwable(causeMessage,null));
    }
}
