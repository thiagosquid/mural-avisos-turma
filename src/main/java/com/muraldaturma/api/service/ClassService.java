package com.muraldaturma.api.service;

import com.muraldaturma.api.dto.ClassDTO;
import com.muraldaturma.api.dto.mapper.ClassMapper;
import com.muraldaturma.api.exception.ClassNotFoundException;
import com.muraldaturma.api.exception.UserNotFoundException;
import com.muraldaturma.api.exception.UsernameAlreadyExistsException;
import com.muraldaturma.api.model.Class;
import com.muraldaturma.api.model.User;
import com.muraldaturma.api.repository.ClassRepository;
import com.muraldaturma.api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ClassService {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassMapper classMapper;

    public List<ClassDTO> getAll() {
        return classMapper.toListDTO(classRepository.findAll());
    }

    public ClassDTO getById(Long classId) {
        Optional<Class> classFound = classRepository.findById(classId);
        if (classFound.isEmpty()) {
            throw new ClassNotFoundException(String.format("Não foi encontrada  turma com esse id: %d", classId), "class.notFound");
        }
        return classMapper.toDTO(classFound.get());
    }

    public ClassDTO create(ClassDTO classToCreateDTO) {
        Class classToCreate = classMapper.toModel(classToCreateDTO);
        return classMapper.toDTO(classRepository.save(classToCreate));
    }

    @Transactional
    public void addStudentAtClass(Long classId, String username) {
        Optional<Class> classToUpdate = classRepository.findById(classId);
        Optional<User> userOptional = userRepository.findByUsername(username);


        if (userOptional.isPresent() && classToUpdate.isPresent()) {
            List<Class> classList = userOptional.get().getClassList();
            if (classList.contains(classToUpdate.get())) {
                throw new UsernameAlreadyExistsException(String.format("O usuário %s já está na turma", username), "user.alreadyExist");
            }
            log.info("===============LINHA 65=========================");
            userOptional.get().setClassList(classList);
            User userSaved = userRepository.save(userOptional.get());
            log.info("===============LINHA 68=========================");

        } else if (userOptional.isEmpty()) {
            throw new UserNotFoundException(String.format("Não foi encontrado usuário com esse username:  %s", username), "user.notFound");
        } else if (classToUpdate.isEmpty()) {
            throw new ClassNotFoundException(String.format("Não foi encontrada  turma com esse id: %d", classId), "class.notFound");
        }

    }
}
