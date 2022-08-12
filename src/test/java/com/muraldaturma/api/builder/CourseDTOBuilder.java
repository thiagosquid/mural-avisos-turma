package com.muraldaturma.api.builder;

import com.muraldaturma.api.dto.CourseDTO;
import lombok.Builder;

@Builder
public class CourseDTOBuilder {

    @Builder.Default
    private Integer id = 1;

    @Builder.Default
    private String name = "Análise e Desenvolvimento de Sistemas";

    @Builder.Default
    private String acronym = "ADS";

    @Builder.Default
    private Integer semesters = 6;

    public CourseDTO toClass() {
        return new CourseDTO(id, name, acronym, semesters);
    }
}
