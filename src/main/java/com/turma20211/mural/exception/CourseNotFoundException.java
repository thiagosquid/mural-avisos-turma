package com.turma20211.mural.exception;

public class CourseNotFoundException extends Exception {
    public CourseNotFoundException(Integer id) {
        super("Curso com ID {"+ id + "} não encontrado!");
    }
}
