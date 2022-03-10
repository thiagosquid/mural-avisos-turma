package com.turma20211.mural.service;

import com.turma20211.mural.exception.TagExistsException;
import com.turma20211.mural.model.Tag;
import com.turma20211.mural.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final ClassService classService;

    public List<Tag> getAll() {
        return tagRepository.findAll();
    }

    public Tag create(Tag tag) throws TagExistsException {
        if (verifyIfExists(tag.getDescription())) {
            throw new TagExistsException(tag.getDescription());
        }
        return tagRepository.save(tag);
    }

    public void deleteById(Integer tagId) throws Exception {
        if (!verifyIfExists(tagId)) {
            throw new Exception("Tag informada não existe");
        }
        tagRepository.deleteById(tagId);
    }

    public void deleteAllById(List<Integer> listTagsIds) throws Exception {
        if (!verifyIfExists(listTagsIds)) {
            throw new Exception("Tags informadas não existem");
        }
        tagRepository.deleteAllById(listTagsIds);
    }

    private boolean verifyIfExists(Integer tagId) {
        Optional<Tag> tagExistent = tagRepository.findById(tagId);
        return tagExistent.isPresent();
    }

    private boolean verifyIfExists(List<Integer> listTagsIds) {
        List<Tag> listExistent = tagRepository.findAllById(listTagsIds);;
        return listExistent.size() == listTagsIds.size();
    }

    private boolean verifyIfExists(String description) {
        Optional<Tag> tagExistent = tagRepository.findByDescription(description);
        return tagExistent.isPresent();
    }
}
