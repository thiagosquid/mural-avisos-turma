package com.muraldaturma.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TagExistsException extends RuntimeException {
    public TagExistsException(String description) {
        super(String.format("Já existe uma TAG com essa descrição: %s",description));
    }
}
