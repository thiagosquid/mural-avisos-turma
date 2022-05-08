package com.muraldaturma.api.dto.mapper;

import com.muraldaturma.api.dto.PostDTO;
import com.muraldaturma.api.model.Class;
import com.muraldaturma.api.model.Post;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

@Named("PostMapper")
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {TagMapper.class, UserMapper.class, ClassMapper.class})
public interface PostMapper {
//    PostMapper INSTANCE = Mappers.getMapper( PostMapper.class );

    Post toModel(PostDTO dto);

    @Named("toDTO")
    @Mappings({
            @Mapping(target = "comments",
                    expression = "java(model.getCommentList().size())"),
            @Mapping(target = "aClass",
                    expression = "java(model.getAClass().toString())")
    })
    PostDTO toDTO(Post model);

    //    @Named("toDTOWithoutTag")
//    @Mappings({
//            @Mapping(target = "tag", ignore = true)
//    })
    PostDTO toDTOWithoutTag(Post model);

//    HashSet<Post> toModel(HashSet<PostDTO> dtoSet);

    List<PostDTO> toListDTO(List<Post> modelList);

    Class map(String value);

}
