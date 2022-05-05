package com.muraldaturma.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostDTO {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private LocalDateTime deadline;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private TagDTO tag;

//    private User user;
//
//    private Class aClass;

//    private List<Comment> commentList = new ArrayList<>();
//
//    private List<User> usersFavorited = new ArrayList<>();
}
