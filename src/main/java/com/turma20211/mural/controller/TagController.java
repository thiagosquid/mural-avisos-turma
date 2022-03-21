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
    public ResponseEntity<?> create(@RequestBody List<Tag> tagList) {
        URI uri;
        try {
            if (tagList.size() == 1) {
                Tag tagSaved = tagService.create(tagList.get(0));
                uri = ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(tagSaved.getId())
                        .toUri();
                log.info("Criada TAG com id \"{}\" e descrição \"{}\"", tagSaved.getId(), tagSaved.getDescription());
                return ResponseEntity.created(uri).body(tagSaved);
            } else {
                try {
                    tagService.createAll(tagList);
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
                }
                log.info("Criadas as seguintes TAG's {}", tagList);
                return ResponseEntity.status(HttpStatus.CREATED).body(tagList);
            }
        } catch (TagExistsException e) {
//            log.error("TAG \"{}\" já existe no banco", tag.getDescription());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("{tagIdList}")
    public ResponseEntity<?> delete(@PathVariable List<Integer> tagIdList) {
        try {
            if (tagIdList.size() == 1) {
                tagService.deleteById(tagIdList.get(0));
                log.info("Deletada tag com id {}", tagIdList.get(0));
            } else {
                tagService.deleteAllById(tagIdList);
                log.info("Deletadas tags com os IDs {}", tagIdList);
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
