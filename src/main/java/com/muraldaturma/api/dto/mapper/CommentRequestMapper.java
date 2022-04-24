package com.muraldaturma.api.dto.mapper;

import com.muraldaturma.api.dto.request.CommentRequestDto;
import com.muraldaturma.api.model.Comment;

public class CommentRequestMapper {

    public static Comment toModel(CommentRequestDto dto){
        Comment model = new Comment(dto.getId(),
                dto.getContent(),
                dto.getCreatedAt(),
                null,
                null);
        return model;
    }

    public static CommentRequestDto toDto(Comment model){
        CommentRequestDto dto = new CommentRequestDto(model.getId(),
                model.getContent(),
                model.getCreatedAt(),
                model.getPost().getId(),
                model.getUser().getId());

        return dto;
    }

}
