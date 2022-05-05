package com.muraldaturma.api.dto.mapper;

import com.muraldaturma.api.dto.TagDTO;
import com.muraldaturma.api.model.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

//@Component
@Mapper(componentModel = "spring", uses = {PostMapper.class}) /*classes mappers que quero o DTO*/
public interface TagMapper {
    TagMapper INSTANCE = Mappers.getMapper( TagMapper.class );

    Tag toModel(TagDTO dto);

    TagDTO toDTO(Tag model);
}
