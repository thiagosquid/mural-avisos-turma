package com.turma20211.mural.service;

import com.turma20211.mural.model.Tag;
import com.turma20211.mural.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public List<Tag> getAll(){
        return tagRepository.findAll();
    }
}
