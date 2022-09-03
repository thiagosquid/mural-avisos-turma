package com.muraldaturma.api.controller;

import com.muraldaturma.api.dto.ClassDTO;
import com.muraldaturma.api.event.CreatedResourceEvent;
import com.muraldaturma.api.service.ClassService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/class")
@Slf4j
public class ClassController {

    private final ClassService classService;

    private final ApplicationEventPublisher publisher;

    public ClassController(ClassService classService, ApplicationEventPublisher publisher) {
        this.classService = classService;
        this.publisher = publisher;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ClassDTO> getAll() {
        return classService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ClassDTO> getById(@PathVariable Long id) {
        ClassDTO classFound;
        classFound = classService.getById(id);
        return ResponseEntity.ok().body(classFound);
    }

    @PostMapping
    public ResponseEntity<ClassDTO> create(@RequestBody ClassDTO classToCreateDTO, HttpServletResponse response) {
        ClassDTO classSaved = classService.create(classToCreateDTO);

        publisher.publishEvent(new CreatedResourceEvent(this, response, classSaved.getId()));
        log.info("Criada a classe {}", classSaved);
        return ResponseEntity.status(HttpStatus.CREATED).body(classSaved);
    }

    @PostMapping("/{classId}")
    public ResponseEntity<?> addStudentAtClass(@PathVariable("classId") Long classId
            , @RequestParam("username") String username) {
        classService.addStudentToClass(classId, username);
        return ResponseEntity.ok().build();
    }

}
