package com.muraldaturma.api.service;

import com.muraldaturma.api.dto.ClassDTO;
import com.muraldaturma.api.dto.mapper.ClassMapper;
import com.muraldaturma.api.dto.mapper.ClassMapperImpl;
import com.muraldaturma.api.exception.ClassNotFoundException;
import com.muraldaturma.api.exception.UserNotFoundException;
import com.muraldaturma.api.exception.UsernameAlreadyExistsException;
import com.muraldaturma.api.model.Class;
import com.muraldaturma.api.model.User;
import com.muraldaturma.api.repository.ClassRepository;
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
    private UserService userService;

    private final ClassMapper classMapper = new ClassMapperImpl();

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
        Class classSaved = classRepository.save(classToCreate);
        return classMapper.toDTO(classSaved);
    }

    @Transactional
    public void addStudentToClass(Long classId, String username) {
        Optional<Class> classToUpdate = classRepository.findById(classId);
        Optional<User> userOptional = userService.findByUsername(username);

        if (userOptional.isPresent() && classToUpdate.isPresent()) {
            List<Class> classList = userOptional.get().getClassList();
            if (classList.contains(classToUpdate.get())) {
                throw new UsernameAlreadyExistsException(String.format("O usuário %s já está na turma", username), "user.alreadyExist");
            }
            userOptional.get().setClassList(classList);
            userService.update(userOptional.get());

        } else if (userOptional.isEmpty()) {
            throw new UserNotFoundException(String.format("Não foi encontrado usuário com esse username: %s", username), "user.notFound");
        } else if (classToUpdate.isEmpty()) {
            throw new ClassNotFoundException(String.format("Não foi encontrada  turma com esse id: %d", classId), "class.notFound");
        }

    }
}
