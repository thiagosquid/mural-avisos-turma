package com.turma20211.mural.data;

import com.turma20211.mural.model.User;
import com.turma20211.mural.utils.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public class UserDetailData implements UserDetails {

    private final Optional<User> user;

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String avatar;
    private Boolean locked;
    private Boolean enabled;
    private String role;


    public UserDetailData(Optional<User> user) {
        this.user = user;
    }

    public Long getId() {
        return user.orElse(new User()).getId();
    }

    public String getFirstName(){
        return user.orElse(new User()).getFirstName();
    }

    public String getLastName(){
        return user.orElse(new User()).getLastName();
    }

    public String getEmail(){
        return user.orElse(new User()).getEmail();
    }

    public String getAvatar(){
        return user.orElse(new User()).getAvatar();
    }

    public String getRole() {
        return user.orElse(new User()).getRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority(user.orElse(new User()).getRole()));
    }

    @Override
    public String getPassword() {
        return user.orElse(new User()).getPassword();
    }

    @Override
    public String getUsername() {
        return user.orElse(new User()).getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.orElse(new User()).getLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.orElse(new User()).getEnabled();
    }
}
