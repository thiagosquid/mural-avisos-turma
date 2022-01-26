package com.turma20211.mural.controller;

import com.turma20211.mural.model.Post;
import com.turma20211.mural.repository.UserRepository;
import com.turma20211.mural.service.PostService;
import com.turma20211.mural.service.UserDetailServiceImpl;
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
    private UserRepository userRepository;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Post> getAll(@RequestParam(required = false, name = "userId") Long userId){
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

    public List<Post> getByUser(Long userId) {

        List<Post> postList = postService.getByUser(userId);

        return postList;
    }


    @PostMapping(value = "/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void insert(@RequestBody Post post, @PathVariable Long userId){

        post.setId(null);
        post.setUser(userRepository.getById(userId));
        postService.insert(post);
    }
}
