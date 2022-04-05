package com.turma20211.mural.dto;

import com.turma20211.mural.model.Class;
import com.turma20211.mural.model.Comment;
import com.turma20211.mural.model.Tag;
import com.turma20211.mural.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostDto {

    private Long id;

    private String title;

    private String content;

    private LocalDateTime deadline;

    private LocalDateTime createdAt;

    private Tag tag;

    private User user;

    private Class aClass;

    private List<Comment> commentList = new ArrayList<>();

    private List<User> usersFavorited = new ArrayList<>();
}
