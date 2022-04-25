package com.muraldaturma.api.repository;

import com.muraldaturma.api.model.User;
import com.muraldaturma.api.model.Comment;
import com.muraldaturma.api.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByUser(User user);
    List<Comment> findByPost(Post post);

}
