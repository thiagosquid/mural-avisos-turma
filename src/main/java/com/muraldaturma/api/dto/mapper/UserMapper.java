package com.muraldaturma.api.dto.mapper;

import com.muraldaturma.api.dto.TagDTO;
import com.muraldaturma.api.dto.UserDTO;
import com.muraldaturma.api.model.Tag;
import com.muraldaturma.api.model.User;
import com.muraldaturma.api.utils.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PostMapper.class})
public interface UserMapper {

    User toModel(UserDTO dto);

//    @Mapping(target = "postsList", qualifiedByName = "noUser")
    UserDTO toDTO(User model);
}
