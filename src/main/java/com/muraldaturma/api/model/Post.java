package com.muraldaturma.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName(value = "post")
@Table(name = "tb_post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private LocalDateTime deadline;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private Class aClass;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> commentList = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "favoritesPosts", fetch = FetchType.LAZY)
    private Set<User> usersFavorited = new HashSet<>();

    @Transient
    private Boolean favorite;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        if (!id.equals(post.id)) return false;
        if (!title.equals(post.title)) return false;
        if (!content.equals(post.content)) return false;
        if (!Objects.equals(deadline, post.deadline)) return false;
        if (!Objects.equals(createdAt, post.createdAt)) return false;
        if (!tag.equals(post.tag)) return false;
        if (!user.equals(post.user)) return false;
        return Objects.equals(aClass, post.aClass);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + content.hashCode();
        result = 31 * result + (deadline != null ? deadline.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + tag.hashCode();
        result = 31 * result + user.hashCode();
        result = 31 * result + (aClass != null ? aClass.hashCode() : 0);
        return result;
    }
}
