package com.turma20211.mural.service;

import com.turma20211.mural.model.User;
import com.turma20211.mural.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }
}
