package com.muraldaturma.api.service;

import com.muraldaturma.api.dto.TagDTO;
import com.muraldaturma.api.dto.mapper.TagMapper;
import com.muraldaturma.api.exception.TagException;
import com.muraldaturma.api.model.Tag;
import com.muraldaturma.api.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagMapper tagMapper;

    public List<TagDTO> getAll() {
        return tagMapper.toListDTO(tagRepository.findAll());
    }

    public TagDTO getById(Integer id) {
        Optional<Tag> tagFound = tagRepository.findById(id);
        if(tagFound.isEmpty()){
            throw new TagException("Tag não encontrada com id: ".concat(id.toString()), "tag.notFound");
        }
        return tagMapper.toDTO(tagFound.get());
    }

    public TagDTO create(TagDTO tagDTO) throws TagException {
        if (verifyIfExists(tagDTO.getDescription())) {
            throw new TagException("Já existe uma tag com essa descrição: ".concat(tagDTO.getDescription()), "tag.exists");
        }

        return tagMapper.toDTO(tagRepository.save(tagMapper.toModel(tagDTO)));
    }

    public void createAll(List<TagDTO> tagList) {
        boolean exists = false;
        StringBuilder tagsExistent = new StringBuilder();
        for (TagDTO tag : tagList) {
            if (verifyIfExists(tag.getDescription())) {
                tagsExistent.append(tag.getDescription()).append(", ");
                exists = true;
            }
        }
        if (exists) {
            tagsExistent.deleteCharAt(tagsExistent.length() - 2);
            throw new TagException("Essas tags já existem: ".concat(tagsExistent.toString().trim()), "tag.exists");
        }
        tagRepository.saveAllAndFlush(tagMapper.toListModel(tagList));
    }

    public void deleteById(Integer tagId) {
        if (!verifyIfExists(tagId)) {
            throw new TagException("Tag informada não existe", "tag.notExistent");
        }
        tagRepository.deleteById(tagId);
    }

    public void deleteAllById(List<Integer> listTagsIds) {
        if (!verifyIfExists(listTagsIds)) {
            throw new TagException("Tags informadas não existe", "tag.notExistent");
        }
        tagRepository.deleteAllById(listTagsIds);
    }

    private boolean verifyIfExists(Integer tagId) {
        Optional<Tag> tagExistent = tagRepository.findById(tagId);
        return tagExistent.isPresent();
    }

    private boolean verifyIfExists(List<Integer> listTagsIds) {
        List<Tag> listExistent = tagRepository.findAllById(listTagsIds);
        return listExistent.size() == listTagsIds.size();
    }

    private boolean verifyIfExists(String description) {
        Optional<Tag> tagExistent = tagRepository.findByDescription(description);
        return tagExistent.isPresent();
    }
}
