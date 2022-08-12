package com.muraldaturma.api.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
//@Builder

@Entity
@Table(name = "tb_tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 30, nullable = false, unique = true)
    private String description;

    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY)
    //@JsonIgnore
    @ToString.Exclude
    private List<Post> postList = new ArrayList<Post>();
}




