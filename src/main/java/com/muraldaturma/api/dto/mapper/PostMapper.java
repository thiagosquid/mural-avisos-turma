package com.muraldaturma.api.dto.mapper;

import com.muraldaturma.api.dto.PostDTO;
import com.muraldaturma.api.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;

@Named("PostMapper")
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {TagMapper.class})
public interface PostMapper {
//    PostMapper INSTANCE = Mappers.getMapper( PostMapper.class );

    Post toModel(PostDTO dto);

    @Named("toDTO")
//    @Mappings({
//            @Mapping(target = "tag", qualifiedByName = {"TagMapper", "toDTOWithoutPost"})
//    })
    PostDTO toDTO(Post model);

    //    @Named("toDTOWithoutTag")
//    @Mappings({
//            @Mapping(target = "tag", ignore = true)
//    })
    PostDTO toDTOWithoutTag(Post model);

//    HashSet<Post> toModel(HashSet<PostDTO> dtoSet);

    List<PostDTO> PostDTO(ArrayList<Post> modelList);

}
