package com.turma20211.mural.controller;

import com.turma20211.mural.exception.CourseAlreadyExistsException;
import com.turma20211.mural.exception.CourseNotFoundException;
import com.turma20211.mural.model.Course;
import com.turma20211.mural.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/course")
@Slf4j
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Course> getAll() {
        return courseService.getAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.FOUND).body(courseService.getById(id));
        } catch (CourseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Course course){
        URI uri;
        try {
            courseService.create(course);
            uri = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(course.getId())
                    .toUri();
            log.info("Criado Curso com ID \"{}\" e NOME \"{}\"", course.getId(), course.getName());
            return ResponseEntity.created(uri).body(course);
        } catch (CourseAlreadyExistsException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteById(@PathVariable Integer id){
        try {
            courseService.deleteById(id);
            log.info("Curso com ID {} deletado com sucesso",id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new HashMap<String, String>().put("message","Curso deletado com sucesso"));
        } catch (CourseNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Course course){
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(courseService.update(id, course));
        } catch (CourseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
