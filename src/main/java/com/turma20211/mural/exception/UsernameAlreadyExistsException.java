package com.turma20211.mural.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UsernameAlreadyExistsException extends Exception{

    public UsernameAlreadyExistsException(String username) {
        super(String.format("Username "+ username + " já existe. Tente outro."));
    }
}
