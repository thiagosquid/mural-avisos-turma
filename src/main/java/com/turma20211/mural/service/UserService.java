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

//TODO implementar exception para email inválido
    public User save(User user) {
        if (user.getEmail().contains("@academico.ifs")) {
            return userRepository.save(user);
        }
        return new User();
    }

//TODO implementar exception para usuário inválido
    public User update(User user){
        Optional<User> oldUser = userRepository.findById(user.getId());
        Optional<User> byUsername = userRepository.findByUsername(user.getUsername());

        if(oldUser.isPresent() && byUsername.isPresent() && oldUser.get().getId() == byUsername.get().getId()){
            return userRepository.save(user);
        }

        return new User();
    }
}
