package com.turma20211.mural.repository;

import com.turma20211.mural.model.Class;
import com.turma20211.mural.model.Post;
import com.turma20211.mural.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findById(Long id);
    List<Post> findByUser(User user);
    Page<Post> findByaClass(Class aClass, Pageable pageable);
}
