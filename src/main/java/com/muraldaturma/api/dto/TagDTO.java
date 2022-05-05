package com.muraldaturma.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.muraldaturma.api.model.Post;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TagDTO {

    private Integer id;

    @NotNull
    private String description;

//    private Set<Post> postList;

}
