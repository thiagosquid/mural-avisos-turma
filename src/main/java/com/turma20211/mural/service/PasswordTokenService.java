package com.turma20211.mural.service;

import com.turma20211.mural.model.ConfirmationToken;
import com.turma20211.mural.model.PasswordToken;
import com.turma20211.mural.model.User;
import com.turma20211.mural.repository.PasswordTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PasswordTokenService {

    @Autowired
    private PasswordTokenRepository passwordTokenRepository;

    public void savePasswordToken(PasswordToken passwordToken){
        passwordTokenRepository.save(passwordToken);
    }

    public PasswordToken getToken(String token) {
        return passwordTokenRepository.findByToken(token);
    }

    public void setConfirmedAt(PasswordToken passwordToken) {
        passwordToken.setConfirmedAt(LocalDateTime.now());
        passwordTokenRepository.save(passwordToken);
    }

    public Optional<PasswordToken> findByUser(User user){
        return passwordTokenRepository.findByUser(user);
    }

    public void delete(PasswordToken passwordToken){
        passwordTokenRepository.delete(passwordToken);
    }
}
