package com.turma20211.mural.service;

import com.turma20211.mural.exception.UserNotFoundException;
import com.turma20211.mural.model.Post;
import com.turma20211.mural.model.User;
import com.turma20211.mural.repository.PostRepository;
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

    public List<Post> getByUser(Long id) throws UserNotFoundException {
        Optional<User> user = userService.findById(id);

        if(user != null){
            return postRepository.findByUser(user.get());
        }
        return new ArrayList<Post>();
    }

    public Post insert(Post post){
        return postRepository.save(post);
    }

    public void delete(Long id){
        postRepository.deleteById(id);
    }
}
