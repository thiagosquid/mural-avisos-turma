package com.muraldaturma.api.dto.mapper;

import com.muraldaturma.api.dto.UserDto;
import com.muraldaturma.api.model.User;

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
                user.getRole(),
                user.getPostsList(),
                user.getTokenList(),
                user.getFavoritesPosts(),
                user.getClassList());

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
                userDto.getRole(),
                userDto.getPostsList(),
                userDto.getTokenList(),
                userDto.getFavoritesPosts(),
                userDto.getClassList());

        return user;
    }

}
