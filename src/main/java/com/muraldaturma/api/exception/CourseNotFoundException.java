package com.muraldaturma.api.exception;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(String description, String causeMessage) {
        super(description, new Throwable(causeMessage,null));
    }
}
