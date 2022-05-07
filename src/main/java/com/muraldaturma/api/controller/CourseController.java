package com.muraldaturma.api.controller;

import com.muraldaturma.api.dto.CourseDTO;
import com.muraldaturma.api.event.CreatedResourceEvent;
import com.muraldaturma.api.exception.CourseNotFoundException;
import com.muraldaturma.api.model.Course;
import com.muraldaturma.api.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/course")
@Slf4j
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CourseDTO> getAll() {
        return courseService.getAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<CourseDTO> getById(@PathVariable Integer id) {
//        try {
        CourseDTO courseDTO = courseService.getById(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(courseDTO);
//        } catch (CourseNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CourseDTO courseDTO, HttpServletResponse response) {

        CourseDTO courseDTOSaved = courseService.create(courseDTO);

        publisher.publishEvent(new CreatedResourceEvent(this, response, courseDTOSaved.getId().longValue()));

        log.info("Criado Curso com ID \"{}\" e NOME \"{}\"", courseDTOSaved.getId(), courseDTOSaved.getName());
        return ResponseEntity.ok().body(courseDTOSaved);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteById(@PathVariable Integer id) {
//        try {
            courseService.deleteById(id);
            log.info("Curso com ID {} deletado com sucesso", id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new HashMap<String, String>().put("message", "Curso deletado com sucesso"));
//        } catch (CourseNotFoundException e) {
//            log.error(e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
    }

    @PutMapping("{id}")
    public ResponseEntity<CourseDTO> update(@PathVariable Integer id, @RequestBody CourseDTO courseDTO) {
//        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(courseService.update(id, courseDTO));
//        } catch (CourseNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
    }

}
