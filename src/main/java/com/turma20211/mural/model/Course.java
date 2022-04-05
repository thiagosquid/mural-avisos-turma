package com.turma20211.mural.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "tb_course")
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 10)
    private String acronym;

    @Column(nullable = false)
    private Integer semesters;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @JsonIgnoreProperties
    private List<Class> classList = new ArrayList<>();

}
