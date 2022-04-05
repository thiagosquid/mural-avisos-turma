package com.turma20211.mural.dto.mapper;

import com.turma20211.mural.dto.PostDto;
import com.turma20211.mural.model.Post;

public class PostMapper {

    public static PostDto toDto(Post post){
        PostDto postDto = new PostDto(post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getDeadline(),
                post.getCreatedAt(),
                post.getTag(),
                post.getUser(),
                post.getAClass(),
                post.getCommentList(),
                post.getUsersFavorited());

        return postDto;
    }

    public static Post toModel(PostDto postDto){
        Post post = new Post(postDto.getId(),
                postDto.getTitle(),
                postDto.getContent(),
                postDto.getDeadline(),
                postDto.getCreatedAt(),
                postDto.getTag(),
                postDto.getUser(),
                postDto.getAClass(),
                postDto.getCommentList(),
                postDto.getUsersFavorited());

        return post;
    }
}
