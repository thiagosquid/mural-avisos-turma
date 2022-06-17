package com.muraldaturma.api.service;

import com.muraldaturma.api.builder.ClassBuilder;
import com.muraldaturma.api.builder.ClassDTOBuilder;
import com.muraldaturma.api.dto.ClassDTO;
import com.muraldaturma.api.model.Class;
import com.muraldaturma.api.repository.ClassRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class ClassServiceTest {

    @Mock
    private ClassRepository classRepository;

    @InjectMocks
    private ClassService classService;

    @Test
    void whenGetAll_thenReturnClassDTOList() {
        // given

        // when
        when(classRepository.findAll()).thenReturn(new ArrayList<>());

        // then
        List<ClassDTO> classDTOList = classService.getAll();

        assertEquals(ArrayList.class, classDTOList.getClass());
    }

    @Test
    void givenClassId_whenGetById_thenReturnClassDTOObject() {
        // given
        Long classId = 1L;
        Class classReturned = ClassBuilder.builder().build().toClass();
        classReturned.setId(1L);
        Optional<Class> classOptionalReturned = Optional.of(classReturned);
        ClassDTO classDTOReturned = ClassDTOBuilder.builder().build().toClass();
        classDTOReturned.setId(classId);

        // when
        when(classRepository.findById(classId)).thenReturn(classOptionalReturned);

        // then
        ClassDTO classDTO = classService.getById(classId);

        assertEquals(classDTOReturned.getYear(), classDTO.getYear());
        assertEquals(classDTOReturned.getId(), classDTO.getId());
        assertEquals(classDTOReturned.getSemester(), classDTO.getSemester());
    }

    @Test
    void givenClassObject_whenSave_thenReturnClassDTO() {
        // given
        ClassDTO classToCreateDTO = ClassDTOBuilder.builder().build().toClass();
        Class classToCreate = ClassBuilder.builder().build().toClass();
        Class classCreated = ClassBuilder.builder().build().toClass();
        ClassDTO classToReturnDTO = ClassDTOBuilder.builder().build().toClass();
        classToReturnDTO.setId(1L);

        // when
        when(classRepository.save(classToCreate)).thenReturn(classCreated);

        //then
        ClassDTO createdClass = classService.create(classToCreateDTO);

        assertEquals(classToReturnDTO.getYear(), createdClass.getYear());
    }

}