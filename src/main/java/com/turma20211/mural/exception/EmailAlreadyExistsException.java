package com.turma20211.mural.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailAlreadyExistsException extends Exception{

    public EmailAlreadyExistsException(String email) {
        super(String.format("Email "+ email + " já existe. Tente outro."));
    }
}
