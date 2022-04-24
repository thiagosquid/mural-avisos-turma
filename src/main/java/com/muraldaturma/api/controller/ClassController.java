package com.muraldaturma.api.controller;

import com.muraldaturma.api.exception.ClassNotFoundException;
import com.muraldaturma.api.exception.UserNotFoundException;
import com.muraldaturma.api.model.Class;
import com.muraldaturma.api.service.ClassService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/class")
@AllArgsConstructor
@Slf4j
public class ClassController {

    private final ClassService classService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Class> getAll() {
        return classService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Class classFound = new Class();
        try {
            classFound = classService.getById(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok().body(classFound);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody Class classToCreate, HttpServletRequest request) {
        Class classSaved = classService.create(classToCreate);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(classSaved.getId())
                .toUri();
        log.info("Criada a classe {}", classSaved);
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/{classId}")
    public ResponseEntity<?> addStudent(@PathVariable("classId") Long classId
            , @RequestParam("username") String username) {
        try {
            classService.addStudent(classId, username);
        } catch (UserNotFoundException | ClassNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

}
