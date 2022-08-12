package com.muraldaturma.api.builder;

import com.muraldaturma.api.model.Class;
import com.muraldaturma.api.model.ConfirmationToken;
import com.muraldaturma.api.model.Post;
import com.muraldaturma.api.model.User;
import com.muraldaturma.api.utils.Role;
import lombok.Builder;

import java.util.*;

@Builder
public class UserBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String username = "thiago";

    @Builder.Default
    private String password = UUID.randomUUID().toString();

    @Builder.Default
    private String firstName = "Thiago";

    @Builder.Default
    private String lastName = "Gomes";

    @Builder.Default
    private String email = "thiago.gomes123@academico.ifs.edu.br";

    @Builder.Default
    private Role role = Role.USER;

    @Builder.Default
    private String avatar = null;

    @Builder.Default
    private List<Post> postsList = new ArrayList<>();

    @Builder.Default
    private List<ConfirmationToken> tokenList = new ArrayList<>();

    @Builder.Default
    private Set<Post> favoritesPosts = new HashSet<>();

    @Builder.Default
    private List<Class> classList = new ArrayList<>();

    public User toClass() {
        return new User(null, username, password, firstName, lastName, email, role, avatar, null, null, postsList, tokenList, favoritesPosts, classList);
    }

}

