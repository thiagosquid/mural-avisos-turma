package com.turma20211.mural.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserInvalidEmailException extends Exception {

    public UserInvalidEmailException() {
        super(String.format("Email inválido, você precisa usar um email academico <\"@academico.ifs\">"));
    }


}
