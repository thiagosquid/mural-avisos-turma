package com.muraldaturma.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Table(name = "tb_class")
public class Class {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer semester;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OrderBy("username ASC")
    @ManyToMany(mappedBy = "classList", fetch = FetchType.LAZY)
    private Set<User> userList = new HashSet<>();

    @OneToMany(mappedBy = "aClass", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private Set<Post> postsList = new HashSet<>();

    //TODO Passar isso para um DTO
//    @Override
//    @JsonProperty(value = "code")
//    public String toString() {
//        String courseReturn = course.getAcronym()
//                .concat(year.toString())
//                .concat(semester.toString());
//
//        return courseReturn;
//    }
}
