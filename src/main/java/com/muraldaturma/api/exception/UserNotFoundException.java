package com.muraldaturma.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends Exception {

    public UserNotFoundException(Long id) {
        super(String.format("Usuário com ID %s não encontrado", id));
    }

    public UserNotFoundException(String username) {
        super(String.format("Usuário com o username %s não encontrado", username));
    }
}
