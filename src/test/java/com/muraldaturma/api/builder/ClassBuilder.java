package com.muraldaturma.api.builder;

import com.muraldaturma.api.model.Class;
import com.muraldaturma.api.model.Course;
import com.muraldaturma.api.model.User;
import lombok.Builder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Builder
public class ClassBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private Integer year = 2021;

    @Builder.Default
    private Integer semester = 1;

    @Builder.Default
    private Course course = new Course(1, "Análise e Desenvolvimento de Sistemas", "ADS", 6, new ArrayList<>());

    @Builder.Default
    private List<User> users = new ArrayList<>();

    public Class toClass(){
        return new Class(null, year, semester, course, new HashSet<>(), new HashSet<>());
    }

}

