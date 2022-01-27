package com.turma20211.mural.service;

import com.turma20211.mural.exception.UserInvalidEmailException;
import com.turma20211.mural.exception.UserNotFoundException;
import com.turma20211.mural.model.User;
import com.turma20211.mural.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> findById(Long id) throws UserNotFoundException {
        return Optional.ofNullable(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }

    public User save(User user) throws UserInvalidEmailException {
        if (user.getEmail().contains("@academico.ifs")) {
            return userRepository.save(user);
        }else{
            throw new UserInvalidEmailException();
        }
    }

    public User update(User user) throws UserNotFoundException {
        User byId = verifyIfExists(user.getId());
        User byUsername = verifyIfExists(user.getUsername());

        if(byId.getId() == byUsername.getId()){
            return userRepository.save(user);
        }

        return new User();
    }

    private User verifyIfExists(Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
    }

    private User verifyIfExists(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(()-> new UserNotFoundException(username));
    }
}
