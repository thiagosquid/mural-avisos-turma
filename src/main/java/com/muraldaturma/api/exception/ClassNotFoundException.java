package com.muraldaturma.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.function.Supplier;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClassNotFoundException extends Exception {

    public ClassNotFoundException(Long id) {
        super(String.format("Turma com ID %s não encontrada", id));
    }

}
