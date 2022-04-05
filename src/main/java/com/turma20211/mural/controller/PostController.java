package com.turma20211.mural.controller;

import com.turma20211.mural.dto.PostDto;
import com.turma20211.mural.dto.mapper.PostMapper;
import com.turma20211.mural.exception.ClassNotFoundException;
import com.turma20211.mural.exception.UserNotFoundException;
import com.turma20211.mural.model.Post;
import com.turma20211.mural.service.PostService;
import com.turma20211.mural.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/post")
public class PostController {

    @Autowired
    private PostService postService;

//    @Autowired
//    private UserService userService;

//    @GetMapping
//    @ResponseStatus(HttpStatus.OK)
//    public List<Post> getAll() {
//
//        return postService.getAll();
//    }

    @GetMapping(value = "/{postId}")
    public ResponseEntity<PostDto> getById(@PathVariable Long postId){
        Optional<Post> post = postService.getById(postId);
        if(post.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(PostMapper.toDto(post.get()));
        }

        return ResponseEntity.notFound().build();

    }

    @GetMapping
    public ResponseEntity<?> getAllPageable(Pageable pageable, @RequestParam("classId") Long classId){
        try {
            Page<Post> postListPageable = postService.getAllByClassPageable(pageable, classId);
            return ResponseEntity.status(HttpStatus.OK).body(postListPageable);
        } catch (ClassNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    public List<Post> getByUser(Long userId) throws UserNotFoundException {

        List<Post> postList = postService.getByUser(userId);

        return postList;
    }


    @PostMapping(value = "/{userId}")
    public ResponseEntity<Post> insert(@RequestBody Post post, @PathVariable Long userId) throws UserNotFoundException {

        post.setId(null);
//        post.setUser(userService.findById(userId).get());
        return ResponseEntity.ok(postService.insert(post));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        postService.delete(id);
    }
}
