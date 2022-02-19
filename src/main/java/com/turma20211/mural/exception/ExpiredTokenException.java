package com.turma20211.mural.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class ExpiredTokenException extends Exception {

    public ExpiredTokenException(String msg) {
        super(String.format(msg));
    }
}
