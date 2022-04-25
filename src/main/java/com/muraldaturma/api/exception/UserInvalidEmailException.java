package com.muraldaturma.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserInvalidEmailException extends Exception {

    public UserInvalidEmailException() {
        super("Email inválido, você precisa usar um email academico <\"@academico.ifs.edu.br\">");
    }

    public UserInvalidEmailException(String text){
        super(text);
    }


}
