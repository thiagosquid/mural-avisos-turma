package com.muraldaturma.api.dto;

import com.muraldaturma.api.model.Class;
import com.muraldaturma.api.model.Comment;
import com.muraldaturma.api.model.Tag;
import com.muraldaturma.api.model.User;
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
