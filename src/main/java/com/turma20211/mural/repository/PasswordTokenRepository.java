package com.turma20211.mural.repository;

import com.turma20211.mural.model.PasswordToken;
import com.turma20211.mural.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordTokenRepository extends JpaRepository<PasswordToken, Long> {
    PasswordToken findByToken(String token);
    Optional<PasswordToken> findByUser(User user);
}
