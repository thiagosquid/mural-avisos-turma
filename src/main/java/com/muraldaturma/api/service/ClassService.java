package com.muraldaturma.api.service;

import com.muraldaturma.api.exception.ClassNotFoundException;
import com.muraldaturma.api.exception.UserNotFoundException;
import com.muraldaturma.api.model.Class;
import com.muraldaturma.api.model.User;
import com.muraldaturma.api.repository.ClassRepository;
import com.muraldaturma.api.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClassService {

    private final ClassRepository classRepository;
    private final UserRepository userRepository;

    public List<Class> getAll() {
        return classRepository.findAll();
    }

    public Class getById(Long id) throws Exception {
        Optional<Class> classFound = classRepository.findById(id);
        if (classFound.isEmpty()) {
            throw new Exception("Turma não encontrada!");
        }
        return classFound.get();
    }

    public Class create(Class classToCreate) {
        return classRepository.save(classToCreate);
    }

    public void addStudent(Long classId, String username) throws UserNotFoundException, ClassNotFoundException {
        Optional<Class> classToUpdate = classRepository.findById(classId);
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent() && classToUpdate.isPresent()) {
            user.get().getClassList().add(classToUpdate.get());
            userRepository.save(user.get());
        } else if (user.isEmpty()) {
            throw new UserNotFoundException(username);
        } else if (classToUpdate.isEmpty()) {
            throw new ClassNotFoundException(classId);
        }

    }
}
