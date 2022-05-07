package com.muraldaturma.api.service;

import com.muraldaturma.api.builder.ClassBuilder;
import com.muraldaturma.api.dto.ClassDTO;
import com.muraldaturma.api.dto.mapper.ClassMapper;
import com.muraldaturma.api.repository.ClassRepository;
import com.muraldaturma.api.model.Class;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class ClassServiceTest {

    @Mock
    private ClassRepository classRepository;

    @InjectMocks
    private ClassService classService;

    @Autowired
    private ClassMapper classMapper;

    @Test
    void whenClassInformedItShouldBeCreated(){
        // given
        Class expectedSavedClass = ClassBuilder.builder().build().toClass();
        Class classToReturn = ClassBuilder.builder().build().toClass();
        classToReturn.setId(1L);
        ClassDTO expectedSavedClassDTO = classMapper.toDTO(expectedSavedClass);
//        ClassDTO classToReturnDTO = classMapper.toDTO(classToReturn);
        // when
        when(classRepository.save(expectedSavedClass)).thenReturn(classToReturn);
        expectedSavedClass.setId(classToReturn.getId());
        //then
        ClassDTO createdClass = classService.create(expectedSavedClassDTO);
        assertEquals(expectedSavedClass, createdClass);

    }

}