package com.turma20211.mural.service;

import com.turma20211.mural.builder.ClassBuilder;
import com.turma20211.mural.model.Class;
import com.turma20211.mural.repository.ClassRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class ClassServiceTest {

    @Mock
    private ClassRepository classRepository;

    @InjectMocks
    private ClassService classService;

    @Test
    void whenClassInformedItShouldBeCreated(){
        // given
        Class expectedSavedClass = ClassBuilder.builder().build().toClass();
        Class classToReturn = ClassBuilder.builder().build().toClass();
        classToReturn.setId(1L);
        // when
        when(classRepository.save(expectedSavedClass)).thenReturn(classToReturn);
        expectedSavedClass.setId(classToReturn.getId());
        //then
        Class createdClass = classService.create(expectedSavedClass);
        assertEquals(expectedSavedClass, createdClass);

    }

}