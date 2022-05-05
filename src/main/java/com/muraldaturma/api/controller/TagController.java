package com.muraldaturma.api.controller;

import com.muraldaturma.api.dto.TagDTO;
import com.muraldaturma.api.event.CreatedResourceEvent;
import com.muraldaturma.api.model.Tag;
import com.muraldaturma.api.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/tag")
@Slf4j
public class TagController {

    @Autowired
    private TagService tagService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Tag> getAll() {
        return tagService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TagDTO getById(@PathVariable(value = "id") Integer id) {
        return tagService.getById(id);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody List<Tag> tagList, HttpServletResponse response) {

            if (tagList.size() == 1) {
                Tag tagSaved = tagService.create(tagList.get(0));
                publisher.publishEvent(new CreatedResourceEvent(this, response, tagSaved.getId().longValue()));
                log.info("Criada TAG com id \"{}\" e descrição \"{}\"", tagSaved.getId(), tagSaved.getDescription());
                return ResponseEntity.status(HttpStatus.CREATED).body(tagSaved);
            } else {
//                try {
                    tagService.createAll(tagList);
//                } catch (Exception e) {
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//                }
                log.info("Criadas as seguintes TAG's {}", tagList);
                return ResponseEntity.status(HttpStatus.CREATED).body(tagList);
            }
//        } catch (TagExistsException e) {
//            log.error(e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
    }

    @DeleteMapping("{tagIdList}")
    public ResponseEntity<?> delete(@PathVariable List<Integer> tagIdList) {
            if (tagIdList.size() == 1) {
                tagService.deleteById(tagIdList.get(0));
                log.info("Deletada tag com id {}", tagIdList.get(0));
            } else {
                tagService.deleteAllById(tagIdList);
                log.info("Deletadas tags com os IDs {}", tagIdList);
            }
            return ResponseEntity.noContent().build();
    }

}
