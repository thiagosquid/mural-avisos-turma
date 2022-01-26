package com.turma20211.mural.repository;

import com.turma20211.mural.model.Comment;
import com.turma20211.mural.model.Post;
import com.turma20211.mural.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByUser(User user);
    List<Comment> findByPost(Post post);

}
