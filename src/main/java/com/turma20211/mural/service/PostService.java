package com.turma20211.mural.service;

import com.turma20211.mural.model.Post;
import com.turma20211.mural.model.User;
import com.turma20211.mural.repository.PostRepository;
import com.turma20211.mural.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    public List<Post> getAll(){
        return postRepository.findAll();
    }

    public Optional<Post> getById(Long id){
        return postRepository.findById(id);
    }

    public List<Post> getByUser(Long id) {
        Optional<User> user = userService.findById(id);

        if(user.isPresent()){
            return postRepository.findByUser(user.get());
        }
        return new ArrayList<Post>();
    }

    public void insert(Post post){
        postRepository.save(post);
    }
}
