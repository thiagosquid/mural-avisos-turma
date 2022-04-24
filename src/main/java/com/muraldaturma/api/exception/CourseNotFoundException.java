package com.muraldaturma.api.exception;

public class CourseNotFoundException extends Exception {
    public CourseNotFoundException(Integer id) {
        super("Curso com ID {"+ id + "} não encontrado!");
    }
}
