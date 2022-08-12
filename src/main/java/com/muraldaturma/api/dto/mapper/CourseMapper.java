package com.muraldaturma.api.dto.mapper;

import com.muraldaturma.api.dto.CourseDTO;
import com.muraldaturma.api.model.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Named("CourseMapper")
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourseMapper {

    Course toModel(CourseDTO dto);

    CourseDTO toDTO(Course model);

    List<CourseDTO> toListDTO(List<Course> modelList);

    List<Course> toListModel(List<CourseDTO> dtoList);

}
