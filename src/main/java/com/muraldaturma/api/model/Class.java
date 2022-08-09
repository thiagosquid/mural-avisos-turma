package com.muraldaturma.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
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
    @Override
//    @JsonProperty(value = "code")
    public String toString() {
        String courseReturn = course.getAcronym()
                .concat(year.toString())
                .concat(semester.toString());

        return courseReturn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Class aClass = (Class) o;

        if (!year.equals(aClass.year)) return false;
        if (!semester.equals(aClass.semester)) return false;
        return course.equals(aClass.course);
    }

    @Override
    public int hashCode() {
        int result = year.hashCode();
        result = 31 * result + semester.hashCode();
        result = 31 * result + course.hashCode();
        return result;
    }
}
