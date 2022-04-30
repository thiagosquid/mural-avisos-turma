package com.muraldaturma.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.muraldaturma.api.model.Class;
import com.muraldaturma.api.model.ConfirmationToken;
import com.muraldaturma.api.model.Post;
import com.muraldaturma.api.utils.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String avatar;
    @JsonIgnore
    private Boolean locked = false;
    @JsonIgnore
    private Boolean enabled = false;
    private Role role;

    @JsonIgnore
    private List<Post> postsList = new ArrayList<>();

    @JsonIgnore
    private List<ConfirmationToken> tokenList = new ArrayList<>();

    private List<Post> favoritesPosts = new ArrayList<>();

    private Set<Class> classList = new HashSet<>();
}
