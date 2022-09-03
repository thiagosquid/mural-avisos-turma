package com.muraldaturma.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TagDTO {

    private Integer id;

    @NotBlank
    private String description;

//    @JsonIgnore
//    private List<PostDTO> postList;

}
