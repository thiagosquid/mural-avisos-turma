package com.muraldaturma.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseDTO {

    private Integer id;

    @NotNull
    private String name;

    @NotNull
    private String acronym;

    @NotNull
    private Integer semesters;
}
