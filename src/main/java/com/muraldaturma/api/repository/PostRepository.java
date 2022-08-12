package com.muraldaturma.api.repository;

import com.muraldaturma.api.dto.PostDTO;
import com.muraldaturma.api.model.Class;
import com.muraldaturma.api.model.User;
import com.muraldaturma.api.model.Post;
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
    Page<Post> findByaClassAndUser(Class aClass, User user, Pageable pageable);
}
