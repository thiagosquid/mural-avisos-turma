package com.muraldaturma.api.controller;

import com.muraldaturma.api.exception.UserNotFoundException;
import com.muraldaturma.api.model.Comment;
import com.muraldaturma.api.service.CommentService;
import com.muraldaturma.api.dto.mapper.CommentRequestMapper;
import com.muraldaturma.api.dto.request.CommentRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

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
