package com.turma20211.mural.repository;

import com.turma20211.mural.model.PasswordToken;
import com.turma20211.mural.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordTokenRepository extends JpaRepository<PasswordToken, Long> {
    PasswordToken findByToken(String token);
}
