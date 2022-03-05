package com.turma20211.mural.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.turma20211.mural.model.Class;
import com.turma20211.mural.model.ConfirmationToken;
import com.turma20211.mural.model.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    private String role;

    @JsonIgnore
    private List<Post> postsList = new ArrayList<>();

    @JsonIgnore
    private List<ConfirmationToken> tokenList = new ArrayList<>();

    private List<Post> favoritesPosts = new ArrayList<>();

    private List<Class> classList = new ArrayList<>();
}
