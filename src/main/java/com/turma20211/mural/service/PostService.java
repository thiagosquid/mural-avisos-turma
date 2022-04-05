package com.turma20211.mural.service;

import com.turma20211.mural.exception.ClassNotFoundException;
import com.turma20211.mural.exception.UserNotFoundException;
import com.turma20211.mural.model.Class;
import com.turma20211.mural.model.Post;
import com.turma20211.mural.model.User;
import com.turma20211.mural.repository.ClassRepository;
import com.turma20211.mural.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    private ClassRepository classRepository;

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

    public Page<Post> getAllByClassPageable(Pageable pageable, Long classId) throws ClassNotFoundException {
        Class classToFilter = classRepository.findById(classId)
                .orElseThrow(() -> new ClassNotFoundException(classId));

        return postRepository.findByaClass(classToFilter, pageable);
    }
}
