package com.muraldaturma.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LastNameInvalidException extends Exception{

    public LastNameInvalidException() {
        super(String.format("O sobrenome não corresponde ao do e-mail! Por favor, verifique o sobrenome ou e-mail."));
    }
}
