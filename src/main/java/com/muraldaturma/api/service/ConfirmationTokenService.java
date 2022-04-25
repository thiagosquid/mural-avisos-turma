package com.muraldaturma.api.service;

import com.muraldaturma.api.model.User;
import com.muraldaturma.api.repository.ConfirmationTokenRepository;
import com.muraldaturma.api.model.ConfirmationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ConfirmationTokenService {

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token){
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public void setConfirmedAt(ConfirmationToken token) {
        token.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> findByUser(User user){
        return confirmationTokenRepository.findByUser(user);
    }

    public Optional<ConfirmationToken> findByUserAndConfirmedAtIsNull(User user){
        return confirmationTokenRepository.findByUserAndConfirmedAtIsNull(user);
    }

    public void delete(ConfirmationToken confirmationToken){
        confirmationTokenRepository.delete(confirmationToken);
    }

}
