package com.turma20211.mural.controller;

import com.turma20211.mural.exception.UserNotFoundException;
import com.turma20211.mural.model.Post;
import com.turma20211.mural.repository.UserRepository;
import com.turma20211.mural.service.PostService;
import com.turma20211.mural.service.UserDetailServiceImpl;
import com.turma20211.mural.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.preauth.RequestAttributeAuthenticationFilter;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Post> getAll(@RequestParam(required = false, name = "userId") Long userId) throws UserNotFoundException {
        if(userId != null){
            return getByUser(userId);
        }
        return postService.getAll();
    }

    @GetMapping(value = "/{postId}")
    public ResponseEntity<Post> getById(@PathVariable Long postId){
        Optional<Post> post = postService.getById(postId);
        if(post.isPresent()){
            return ResponseEntity.status(HttpStatus.FOUND).body(post.get());
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
    public void delete(@PathVariable Long id){
        postService.delete(id);
    }
}
