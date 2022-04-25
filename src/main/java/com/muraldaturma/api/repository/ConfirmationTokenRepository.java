package com.muraldaturma.api.repository;

import com.muraldaturma.api.model.User;
import com.muraldaturma.api.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByToken(String token);
    Optional<ConfirmationToken> findByUser(User user);
    Optional<ConfirmationToken> findByUserAndConfirmedAtIsNull(User user);

}
