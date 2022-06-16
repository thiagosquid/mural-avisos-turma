package com.muraldaturma.api.controller;

import com.muraldaturma.api.dto.PostDTO;
import com.muraldaturma.api.event.CreatedResourceEvent;
import com.muraldaturma.api.exception.UserNotFoundException;
import com.muraldaturma.api.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private ApplicationEventPublisher publisher;

//    @GetMapping
//    @ResponseStatus(HttpStatus.OK)
//    public List<Post> getAll() {
//
//        return postService.getAll();
//    }

    @GetMapping
    public ResponseEntity<Page<PostDTO>> getAllPageable(@RequestParam("classId") Long classId, Pageable pageable) {
        Page<PostDTO> postListPageable = postService.getAllByClassPageable(pageable, classId);
        return ResponseEntity.status(HttpStatus.OK).body(postListPageable);
    }

    @GetMapping(value = "/{postId}")
    public ResponseEntity<PostDTO> getById(@PathVariable Long postId) {
        PostDTO postDTO = postService.getDTOById(postId);
        return ResponseEntity.status(HttpStatus.OK).body(postDTO);

    }

    @GetMapping("/a")
    public ResponseEntity<Page<PostDTO>>  getByUser(@RequestParam Long userId,
                                                    @RequestParam Long classId,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) throws UserNotFoundException {
        Pageable pageable = PageRequest.of(page, size);
        return  ResponseEntity.ok().body(postService.getAllPageable(pageable, userId, classId));
//        return postService.getByUser(userId);
    }

    @PostMapping
    public ResponseEntity<PostDTO> insert(@RequestBody PostDTO postDTO
            , @RequestParam(value = "userId") Long userId, @RequestParam(value = "classId") Long classId
            , HttpServletResponse response) {

        postDTO.setId(null);
        PostDTO postSavedDTO = postService.insert(postDTO, userId, classId);
        publisher.publishEvent(new CreatedResourceEvent(this, response, postSavedDTO.getId()));
        log.info("Criado Post com ID \"{}\" pelo usuário \"{}\"", postSavedDTO.getId(), postSavedDTO.getUser().getUsername());

        return ResponseEntity.ok(postSavedDTO);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        postService.delete(id);
    }
}
