package com.muraldaturma.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TagDTO {

    private Integer id;

    @NotNull
    private String description;

//    @JsonIgnore
//    private List<PostDTO> postList;

}
