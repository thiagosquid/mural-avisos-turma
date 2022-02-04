package com.turma20211.mural.repository;

import com.turma20211.mural.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

//    @Transactional
//    @Modifying
    @Query(value = "update public.usuarios set usuarios.enabled = ?1 WHERE usuarios.username = ?2", nativeQuery = true)
    void enableUser(boolean b, String username);
}
