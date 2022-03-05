package com.turma20211.mural.service;

import com.turma20211.mural.model.Class;
import com.turma20211.mural.repository.ClassRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClassService {

    private final ClassRepository classRepository;

    public List<Class> getAll() {
        return classRepository.findAll();
    }

    public Class getById(Long id){
        Class c = classRepository.getById(id);
        return c;
    }

    public Class create(Class classToCreate){
        return  classRepository.save(classToCreate);
    }
}
