package com.turma20211.mural.service;

import com.turma20211.mural.exception.TagExistsException;
import com.turma20211.mural.model.Class;
import com.turma20211.mural.model.Tag;
import com.turma20211.mural.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final ClassService classService;

    public List<Tag> getAll(Long classId) {
        Class currentClass = classService.getById(classId);
        return tagRepository.findByClass(currentClass.getId());
    }

    public Tag create(Tag tag, Long classId) throws TagExistsException {
        Class classToSave = classService.getById(classId);
        Optional<Tag> tagExistent = tagRepository.findByDescriptionAndAClass(tag.getDescription(), classToSave.getId());
//        Tag tagExistent2 = tagRepository.findByDescriptionAndAClass(tag.getDescription(), classToSave.getId());
        if (tagExistent.isPresent()) {
            throw new TagExistsException(tagExistent.get().getDescription());
        }
        tag.setAClass(classToSave);
        return tagRepository.save(tag);
    }
}
