package com.turma20211.mural.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Date deadline;

    @Column(nullable = false)
    private Date dateRegister;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
