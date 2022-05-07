package com.muraldaturma.api.dto.mapper;

import com.muraldaturma.api.dto.TagDTO;
import com.muraldaturma.api.model.Tag;
import org.mapstruct.*;

import java.util.List;

@Named("TagMapper")
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE) /* uses = {PostMapper.class} classes mappers que quero o DTO*/
public interface TagMapper {
//    TagMapper INSTANCE = Mappers.getMapper( TagMapper.class );

    Tag toModel(TagDTO dto);

    @Named("toDTO")
    @Mappings({
//            @Mapping(target = "postList", qualifiedByName = {"PostMapper", "toDTOWithoutTag"})
    })
    TagDTO toDTO(Tag model);

//    @Named("toDTOWithoutPost")
//    @Mappings({
//            @Mapping(target = "postList", ignore = true)
//    })
//    TagDTO toDTOWithoutPost(Tag model);

    @Mappings({
//            @Mapping(target = "postList", ignore = true)
    })
    List<TagDTO> toListDTO(List<Tag> modelList);

    @Mappings({
            @Mapping(target = "postList", ignore = true)
    })
    List<Tag> toListModel(List<TagDTO> dtoList);

    TagDTO map(Tag value);
}
