package com.turma20211.mural.dto.mapper;

import com.turma20211.mural.dto.UserDto;
import com.turma20211.mural.model.User;

public class UserMapper {

    public static UserDto toDto(User user){
        UserDto userDto = new UserDto(user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAvatar(),
                user.getLocked(),
                user.getEnabled(),
                user.getPostsList());

        return userDto;
    }

    public static User toModel(UserDto userDto){
        User user = new User(userDto.getId(),
                userDto.getUsername(),
                "",
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getEmail(),
                userDto.getAvatar(),
                userDto.getLocked(),
                userDto.getEnabled(),
                userDto.getPostsList());

        return user;
    }

}
