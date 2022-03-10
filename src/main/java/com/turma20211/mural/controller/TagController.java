package com.turma20211.mural.controller;

import com.turma20211.mural.exception.TagExistsException;
import com.turma20211.mural.model.Tag;
import com.turma20211.mural.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/tag")
@RequiredArgsConstructor
@Slf4j
public class TagController {

    private final TagService tagService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Tag> getAll() {
        return tagService.getAll();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Tag tag) {
        URI uri = null;
        try {
            Tag tagSaved = tagService.create(tag);
            uri = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(tagSaved.getId())
                    .toUri();
            log.info("Criada TAG com id \"{}\" e descrição \"{}\"", tagSaved.getId(), tagSaved.getDescription());
        } catch (TagExistsException e) {
            log.error("TAG \"{}\" já existe no banco", tag.getDescription());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("{listTagsIds}")
    public ResponseEntity<?> delete(@PathVariable List<Integer> listTagsIds) {
        try {
            if (listTagsIds.size() == 1) {
                tagService.deleteById(listTagsIds.get(0));
                log.info("Deletada tag com id {}", listTagsIds.get(0));
            } else {
                tagService.deleteAllById(listTagsIds);
                log.info("Deletadas tags com os IDs {}", listTagsIds.toString());
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
