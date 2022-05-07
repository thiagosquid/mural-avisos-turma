package com.muraldaturma.api.dto;

import com.muraldaturma.api.model.Course;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ClassDTO {

    private Long id;

    @NotNull
    private Integer year;

    @NotNull
    private Integer semester;

    @NotNull
    private Course course;

}
