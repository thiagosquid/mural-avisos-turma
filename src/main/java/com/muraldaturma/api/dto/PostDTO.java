package com.muraldaturma.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.muraldaturma.api.model.Class;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
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
//    @JsonIgnoreProperties(value = "postList")
    private TagDTO tag;

    @NotNull
//    @JsonIgnoreProperties(value = "postList")
    private UserDTO user;

    @JsonProperty(value = "class")
    private String aClass;

    private Integer comments;
//
//    private List<User> usersFavorited = new ArrayList<>();
}
