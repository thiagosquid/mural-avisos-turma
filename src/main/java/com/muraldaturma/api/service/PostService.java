package com.muraldaturma.api.service;

import com.muraldaturma.api.dto.PostDTO;
import com.muraldaturma.api.dto.mapper.ClassMapper;
import com.muraldaturma.api.dto.mapper.PostMapper;
import com.muraldaturma.api.exception.ClassNotFoundException;
import com.muraldaturma.api.exception.UserNotFoundException;
import com.muraldaturma.api.model.Class;
import com.muraldaturma.api.model.User;
import com.muraldaturma.api.repository.ClassRepository;
import com.muraldaturma.api.model.Post;
import com.muraldaturma.api.repository.PostRepository;
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
    private PostMapper postMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private ClassService classService;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private ClassMapper classMapper;

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

    public PostDTO insert(PostDTO postDTO, Long userId, Long classId){
        Class aClass = classMapper.toModel(classService.getById(classId));
        User user = userService.findById(userId).get();
        Post post = postMapper.toModel(postDTO);
        post.setUser(user);
        post.setAClass(aClass);
        return postMapper.toDTO(postRepository.save(post));
    }

    public void delete(Long id){
        postRepository.deleteById(id);
    }

    public Page<Post> getAllByClassPageable(Pageable pageable, Long classId) throws ClassNotFoundException {
        Class classToFilter = classRepository.findById(classId)
                .orElseThrow(() -> new ClassNotFoundException(String.format("Não foi encontrada classe com o id:  %d",classId),"class.notFound"));

        return postRepository.findByaClass(classToFilter, pageable);
    }
}
