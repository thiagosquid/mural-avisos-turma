package com.turma20211.mural.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.turma20211.mural.model.Comment;
import com.turma20211.mural.model.Tag;
import com.turma20211.mural.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostDto {

    private Long id;

    private String title;

    private String content;

    private Date deadline;

    private Date dateRegister;

    private Tag tag;

    private User user;

    private List<Comment> commentList = new ArrayList<>();

}
