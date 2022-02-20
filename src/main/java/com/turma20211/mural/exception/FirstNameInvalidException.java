package com.turma20211.mural.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FirstNameInvalidException extends Exception{

    public FirstNameInvalidException() {
        super(String.format("O nome não corresponde ao do e-mail! Por favor, verifique o nome ou e-mail."));
    }
}
