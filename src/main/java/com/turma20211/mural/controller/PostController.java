package com.turma20211.mural.controller;

import com.turma20211.mural.dto.PostDto;
import com.turma20211.mural.dto.mapper.PostMapper;
import com.turma20211.mural.exception.UserNotFoundException;
import com.turma20211.mural.model.Post;
import com.turma20211.mural.service.PostService;
import com.turma20211.mural.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/post")
@CrossOrigin(value = "*")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Post> getAll() {

        return postService.getAll();
    }

    @GetMapping(value = "/{postId}")
    public ResponseEntity<PostDto> getById(@PathVariable Long postId){
        Optional<Post> post = postService.getById(postId);
        if(post.isPresent()){
            return ResponseEntity.status(HttpStatus.FOUND).body(PostMapper.toDto(post.get()));
        }

        return ResponseEntity.notFound().build();

    }

    public List<Post> getByUser(Long userId) throws UserNotFoundException {

        List<Post> postList = postService.getByUser(userId);

        return postList;
    }


    @PostMapping(value = "/{userId}")
    public ResponseEntity<Post> insert(@RequestBody Post post, @PathVariable Long userId) throws UserNotFoundException {

        post.setId(null);
        post.setUser(userService.findById(userId).get());
        return ResponseEntity.ok(postService.insert(post));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        postService.delete(id);
    }
}
