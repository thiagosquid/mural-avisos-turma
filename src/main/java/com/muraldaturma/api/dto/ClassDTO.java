package com.muraldaturma.api.dto;

import com.muraldaturma.api.model.Course;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsExclude;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ClassDTO {

    @EqualsExclude
    private Long id;

    @NotNull
    private Integer year;

    @NotNull
    private Integer semester;

    @NotNull
    private CourseDTO course;

//    @Override
//    public String toString() {
//        return course.getAcronym().concat(year.toString()).concat(semester.toString());
//    }
}
