package com.turma20211.mural.controller;

import com.turma20211.mural.model.Class;
import com.turma20211.mural.service.ClassService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/class")
@AllArgsConstructor
public class ClassController {

    private final ClassService classService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Class> getAll(){
        return classService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Class> getById(@PathVariable Long id){
        Class c = classService.getById(id);
        return ResponseEntity.ok().body(c);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Class classToCreate){
        Class classSaved = classService.create(classToCreate);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(classSaved.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

}
