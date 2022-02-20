package com.turma20211.mural.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailAlreadyExistsException extends Exception{

    public EmailAlreadyExistsException() {
        super(String.format("Já existe um usuário cadastrado com este e-mail. Por favor, tente outro e-mail!"));
    }
}
