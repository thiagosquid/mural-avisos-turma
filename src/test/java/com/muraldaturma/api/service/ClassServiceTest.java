package com.muraldaturma.api.service;

import com.muraldaturma.api.builder.ClassBuilder;
import com.muraldaturma.api.builder.ClassDTOBuilder;
import com.muraldaturma.api.builder.UserBuilder;
import com.muraldaturma.api.dto.ClassDTO;
import com.muraldaturma.api.dto.mapper.ClassMapper;
import com.muraldaturma.api.exception.ClassNotFoundException;
import com.muraldaturma.api.exception.UserNotFoundException;
import com.muraldaturma.api.exception.UsernameAlreadyExistsException;
import com.muraldaturma.api.model.Class;
import com.muraldaturma.api.model.User;
import com.muraldaturma.api.repository.ClassRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class ClassServiceTest {

    @Mock
    private ClassRepository classRepository;

    @InjectMocks
    private ClassService classService;

    @Mock
    private UserService userService;


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
    void givenClassIdNonexistent_whenGetById_thenThowsClassNotFoundException() {
        // given
        Long classId = 2L;
        Optional<Class> classOptionalReturned = Optional.empty();

        // when
        when(classRepository.findById(classId)).thenReturn(classOptionalReturned);

        // then
        ClassNotFoundException exception = assertThrows(ClassNotFoundException.class, ()->{
            classService.getById(classId);
        });

        assertEquals("Não foi encontrada  turma com esse id: ".concat(classId.toString()), exception.getMessage());
        assertEquals(ClassNotFoundException.class, exception.getClass());

    }

    @Test
    void givenClassObject_whenSave_thenReturnClassDTO() {
        // given
        ClassDTO classToCreateDTO = ClassDTOBuilder.builder().build().toClass();
        Class classCreated = ClassBuilder.builder().build().toClass();
        ClassDTO classToReturnDTO = ClassDTOBuilder.builder().build().toClass();
        classToReturnDTO.setId(1L);

        // when
        when(classRepository.save(ArgumentMatchers.any(Class.class))).thenReturn(classCreated);

        //then
        ClassDTO createdClass = classService.create(classToCreateDTO);

        assertEquals(classToReturnDTO.getYear(), createdClass.getYear());
    }

    @Test
    void givenAClassIdAndAStudentUsername_whenAddStudentToClass_thenDoIt(){
        // given
        Long classId = 1L;
        String username = "thiago";
        Optional<User> optionalUserReturned = Optional.of(UserBuilder.builder().build().toClass());

        // when
        when(classRepository.findById(classId)).thenReturn(Optional.of(ClassBuilder.builder().build().toClass()));
        when(userService.findByUsername(username)).thenReturn(optionalUserReturned);
        willDoNothing().given(userService).update(optionalUserReturned.get());

        // then
        classService.addStudentToClass(classId, username);

        verify(classRepository, times(1)).findById(classId);
        verify(userService, times(1)).findByUsername(username);
        verify(userService, times(1)).update(optionalUserReturned.get());
    }

    @Test
    void givenAClassIdExistentAndAUsernameNonexistent_whenAddStudentToClass_thenThrowClassNotFoundException(){
        // given
        Long classId = 1L;
        String username = "marcos";
        Optional<User> optionalUserReturned = Optional.empty();

        // when
        when(classRepository.findById(classId)).thenReturn(Optional.of(ClassBuilder.builder().build().toClass()));
        when(userService.findByUsername(username)).thenReturn(optionalUserReturned);

        String message = Assertions.assertThrows(UserNotFoundException.class, ()->{
            classService.addStudentToClass(classId, username);
        }).getMessage();

        verify(userService, never()).update(any(User.class));
        assertEquals("Não foi encontrado usuário com esse username: marcos", message);
    }

    @Test
    void givenAClassIdWithAUsernameAlreadyExistent_whenAddStudentToClass_thenThrowUsernameAlreadyExistsException(){
        // given
        Long classId = 1L;
        String username = "thiago";
        Optional<User> optionalUserReturned = Optional.of(UserBuilder.builder().build().toClass());
        Optional<Class> optionalClassReturned = Optional.of(ClassBuilder.builder().build().toClass());
        optionalClassReturned.get().getUserList().add(optionalUserReturned.get());
        optionalUserReturned.get().getClassList().add(optionalClassReturned.get());

        // when
        when(classRepository.findById(classId)).thenReturn(optionalClassReturned);
        when(userService.findByUsername(username)).thenReturn(optionalUserReturned);

        UsernameAlreadyExistsException exception = Assertions.assertThrows(UsernameAlreadyExistsException.class, ()->{
            classService.addStudentToClass(classId, username);
        });

        verify(userService, never()).update(any(User.class));
        assertEquals("O usuário thiago já está na turma", exception.getMessage());
        assertEquals(UsernameAlreadyExistsException.class, exception.getClass());
    }

    @Test
    void givenAClassIdNonexistentAndAUsernameExistent_whenAddStudentToClass_thenThrowClassNotFoundException(){
        // given
        Long classId = 2L;
        String username = "thiago";
        Optional<User> optionalUserReturned = Optional.of(UserBuilder.builder().build().toClass());
        Optional<Class> optionalClassReturned = Optional.empty();

        // when
        when(classRepository.findById(classId)).thenReturn(optionalClassReturned);
        when(userService.findByUsername(username)).thenReturn(optionalUserReturned);

        ClassNotFoundException exception = Assertions.assertThrows(ClassNotFoundException.class, ()->{
            classService.addStudentToClass(classId, username);
        });

        // then
        verify(userService, never()).update(any(User.class));
        assertEquals("Não foi encontrada  turma com esse id: ".concat(classId.toString()), exception.getMessage());
        assertEquals(ClassNotFoundException.class, exception.getClass());
    }

}