package com.turma20211.mural.controller;

import com.turma20211.mural.dto.mapper.CommentRequestMapper;
import com.turma20211.mural.dto.request.CommentRequestDto;
import com.turma20211.mural.model.Comment;
import com.turma20211.mural.model.Post;
import com.turma20211.mural.service.CommentService;
import com.turma20211.mural.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public Comment makeComment(@RequestBody CommentRequestDto commentDto){
        Comment comment = CommentRequestMapper.toModel(commentDto);

        return commentService.makeComment(commentDto.getPostId(), commentDto.getUserId(), comment);
    }
}
