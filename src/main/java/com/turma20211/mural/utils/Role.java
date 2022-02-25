package com.turma20211.mural.utils;

public enum Role {
    NORMAL("NORMAL"),
    SUPERUSER("SUPERUSER"),
    ADMIN("ADMIN");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
