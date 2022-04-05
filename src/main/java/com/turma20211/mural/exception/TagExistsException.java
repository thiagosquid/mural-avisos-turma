package com.turma20211.mural.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TagExistsException extends Exception {
    public TagExistsException(String description) {
        super(String.format("Já existe uma TAG com essa descrição: %s",description));
    }
}
