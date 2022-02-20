package com.turma20211.mural.service;

import com.turma20211.mural.model.ConfirmationToken;
import com.turma20211.mural.model.User;
import com.turma20211.mural.repository.ConfirmationTokenRepository;
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

    public void delete(ConfirmationToken confirmationToken){
        confirmationTokenRepository.delete(confirmationToken);
    }

}