package com.muraldaturma.api.builder;

import com.muraldaturma.api.dto.ClassDTO;
import com.muraldaturma.api.dto.CourseDTO;
import com.muraldaturma.api.model.User;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public class ClassDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private Integer year = 2021;

    @Builder.Default
    private Integer semester = 1;

    @Builder.Default
    private CourseDTO course = CourseDTOBuilder.builder().build().toClass();

    @Builder.Default
    private List<User> users = new ArrayList<>();

    public ClassDTO toClass() {
        return new ClassDTO(null, year, semester, course);
    }

}

