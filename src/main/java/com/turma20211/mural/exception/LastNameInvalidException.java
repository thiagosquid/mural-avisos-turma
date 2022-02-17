package com.turma20211.mural.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LastNameInvalidException extends Exception{

    public LastNameInvalidException(String firstName, String lastNameEmail) {
        super(String.format("Ultimo nome {"+ firstName + "} está diferente do email {" + lastNameEmail +"}"));
    }
}
