package com.turma20211.mural.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends Exception {

    public UserNotFoundException(Long id) {
        super(String.format("User with ID %s not found", id));
    }

    public UserNotFoundException(String username) {
        super(String.format("User with Username %s not found", username));
    }
}
