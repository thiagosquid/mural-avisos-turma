package com.muraldaturma.api.dto.mapper;


import com.muraldaturma.api.dto.ClassDTO;
import com.muraldaturma.api.model.Class;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Named("ClassMapper")
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClassMapper {

    Class toModel(ClassDTO dto);

    ClassDTO toDTO(Class model);

    List<ClassDTO> toListDTO(List<Class> modelList);

    List<Class> toListModel(List<ClassDTO> dtoList);

    Class map(String value);
}
