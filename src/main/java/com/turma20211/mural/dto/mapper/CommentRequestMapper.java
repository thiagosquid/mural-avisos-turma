package com.turma20211.mural.dto.mapper;

import com.turma20211.mural.dto.request.CommentRequestDto;
import com.turma20211.mural.model.Comment;

public class CommentRequestMapper {

    public static Comment toModel(CommentRequestDto dto){
        Comment model = new Comment(dto.getId(),
                dto.getContent(),
                dto.getDateRegister(),
                null,
                null);
        return model;
    }

    public static CommentRequestDto toDto(Comment model){
        CommentRequestDto dto = new CommentRequestDto(model.getId(),
                model.getContent(),
                model.getDateRegister(),
                model.getPost().getId(),
                model.getUser().getId());

        return dto;
    }

}
