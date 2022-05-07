package com.muraldaturma.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.muraldaturma.api.model.Class;
import com.muraldaturma.api.model.ConfirmationToken;
import com.muraldaturma.api.model.Post;
import com.muraldaturma.api.utils.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String avatar;
//    private Boolean locked = false;
//    private Boolean enabled = false;
    private String role;

//    @JsonIgnoreProperties(value = "user")
//    private List<PostDTO> postsList = new ArrayList<>();

//    private List<ConfirmationToken> tokenList = new ArrayList<>();
//
//    private List<Post> favoritesPosts = new ArrayList<>();

//    private Set<Class> classList = new HashSet<>();
}
