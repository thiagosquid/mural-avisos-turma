package com.muraldaturma.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CourseAlreadyExistsException extends Exception {
    public CourseAlreadyExistsException(String name) {
        super("Curso com o nome {" + name + "} já existe!");
    }
}
