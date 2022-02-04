package com.turma20211.mural.repository;

import com.turma20211.mural.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByToken(String token);

    @Query(value = "update public.confirmation_token set confirmed_at = ?1 where token = ?2", nativeQuery = true)
    void updateConfirmedAt(LocalDateTime confirmedAt, String token);
}
