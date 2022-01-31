package com.turma20211.mural.controller;

import com.turma20211.mural.dto.mapper.CommentRequestMapper;
import com.turma20211.mural.dto.request.CommentRequestDto;
import com.turma20211.mural.exception.UserNotFoundException;
import com.turma20211.mural.model.Comment;
import com.turma20211.mural.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
@CrossOrigin(value = "*")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public CommentRequestDto makeComment(@RequestBody CommentRequestDto commentDto) throws UserNotFoundException {
        Comment comment = CommentRequestMapper.toModel(commentDto);
        Comment commentResponse = commentService.makeComment(commentDto.getPostId(), commentDto.getUserId(), comment);
        return CommentRequestMapper.toDto(commentResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        commentService.delete(id);
    }
}
