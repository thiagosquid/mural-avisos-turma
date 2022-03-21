package com.turma20211.mural.exception;

public class CourseAlreadyExistsException extends Exception {
    public CourseAlreadyExistsException(String name) {
        super("Curso com o nome {" + name + "} já existe!");
    }
}
