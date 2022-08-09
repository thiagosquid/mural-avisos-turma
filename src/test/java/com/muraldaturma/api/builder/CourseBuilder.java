package com.muraldaturma.api.builder;

import com.muraldaturma.api.model.Class;
import com.muraldaturma.api.model.Course;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public class CourseBuilder {

    @Builder.Default
    private Integer id = 1;

    @Builder.Default
    private String name = "Análise e Desenvolvimento de Sistemas";

    @Builder.Default
    private String acronym = "ADS";

    @Builder.Default
    private Integer semesters = 6;

    @Builder.Default
    private List<Class> classList = new ArrayList<>();

    public Course toClass(){
        return new Course(id, name, acronym, semesters, classList);
    }
}
