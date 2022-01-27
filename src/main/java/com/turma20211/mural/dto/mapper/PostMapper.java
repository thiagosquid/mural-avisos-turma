package com.turma20211.mural.dto.mapper;

import com.turma20211.mural.dto.PostDto;
import com.turma20211.mural.dto.UserDto;
import com.turma20211.mural.model.Post;
import com.turma20211.mural.model.User;

public class PostMapper {

    public static PostDto toDto(Post post){
        PostDto postDto = new PostDto(post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getDeadline(),
                post.getDateRegister(),
                post.getTag(),
                post.getUser(),
                post.getCommentList());

        return postDto;
    }

    public static Post toModel(PostDto postDto){
        Post post = new Post(postDto.getId(),
                postDto.getTitle(),
                postDto.getContent(),
                postDto.getDeadline(),
                postDto.getDateRegister(),
                postDto.getTag(),
                postDto.getUser(),
                postDto.getCommentList());

        return post;
    }
}
