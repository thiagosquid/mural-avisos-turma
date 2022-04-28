package com.muraldaturma.api.dto.mapper;

import com.muraldaturma.api.dto.UserDto;
import com.muraldaturma.api.model.User;
import com.muraldaturma.api.utils.Role;

public class UserMapper {

    public static UserDto toDto(User user){
        return new UserDto(user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAvatar(),
                user.getLocked(),
                user.getEnabled(),
                user.getRole().toString(),
                user.getPostsList(),
                user.getTokenList(),
                user.getFavoritesPosts(),
                user.getClassList());
    }

    public static User toModel(UserDto userDto){
        return new User(userDto.getId(),
                        userDto.getUsername(),
                        "",
                        userDto.getFirstName(),
                        userDto.getLastName(),
                        userDto.getEmail(),
                        userDto.getRole() != null ? Role.valueOf(userDto.getRole()) : Role.USER,
                        userDto.getAvatar(),
                        userDto.getLocked(),
                        userDto.getEnabled(),
                        userDto.getPostsList(),
                        userDto.getTokenList(),
                        userDto.getFavoritesPosts(),
                        userDto.getClassList());
    }

}
