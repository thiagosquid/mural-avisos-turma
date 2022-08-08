package com.muraldaturma.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.muraldaturma.api.model.Class;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostDTO {

    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private LocalDateTime deadline;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @NotNull
    private TagDTO tag;

    @NotNull
    private UserDTO user;

    @JsonProperty(value = "class")
    private String aClass;

    private Integer comments;

    private Boolean favorite;
//
//    private List<User> usersFavorited = new ArrayList<>();
}
