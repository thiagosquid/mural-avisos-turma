package com.turma20211.mural.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FirstNameInvalidException extends Exception{

    public FirstNameInvalidException(String firstName, String firstNameEmail) {
        super(String.format("Primeiro nome {"+ firstName + "} está diferente do email {" + firstNameEmail +"}"));
    }
}
