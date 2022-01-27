package com.turma20211.mural.controller;

import com.turma20211.mural.model.Tag;
import com.turma20211.mural.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/tag")
@CrossOrigin(value = "*")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Tag> getAll(){
        return tagService.getAll();
    }
}
