package com.muraldaturma.api.service;

import com.muraldaturma.api.dto.PostDTO;
import com.muraldaturma.api.dto.mapper.ClassMapper;
import com.muraldaturma.api.dto.mapper.PostMapper;
import com.muraldaturma.api.exception.ClassNotFoundException;
import com.muraldaturma.api.exception.PostNotFoundException;
import com.muraldaturma.api.exception.UserNotFoundException;
import com.muraldaturma.api.model.Class;
import com.muraldaturma.api.model.Post;
import com.muraldaturma.api.model.User;
import com.muraldaturma.api.repository.ClassRepository;
import com.muraldaturma.api.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.muraldaturma.api.security.JWTValidateFilter.REQUEST_USER_ID;

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

    public List<PostDTO> getAll() {
        return postMapper.toListDTO(postRepository.findAll());
    }

    public PostDTO getDTOById(Long postId) {
        Optional<Post> postFound = postRepository.findById(postId);
        User user = userService.findById(REQUEST_USER_ID).get();
        if (postFound.isPresent()) {

            if (postFound.get().getUsersFavorited().contains(user)) {
                postFound.get().setFavorite(true);
            } else {
                postFound.get().setFavorite(false);
            }
            return postMapper.toDTO(postFound.get());
        }
        throw new PostNotFoundException(String.format("Postagem com o id %d não foi encontrada", postId), "post.notFound");
    }

    public Optional<Post> getModelById(Long id) {

        return postRepository.findById(id);
    }

    public List<PostDTO> getByUser(Long id) throws UserNotFoundException {
        Optional<User> user = userService.findById(id);

        if (user.isPresent()) {
            List<Post> postList = postRepository.findByUser(user.get());
            List<PostDTO> postListDTO = new ArrayList<>(postList.stream().map(postMapper::toDTO).collect(Collectors.toList()));
            return postListDTO;
        }
        return new ArrayList<PostDTO>();
    }

    public PostDTO insert(PostDTO postDTO, Long userId, Long classId) {
        Class aClass = classMapper.toModel(classService.getById(classId));
        User user = userService.findById(userId).get();
        Post post = postMapper.toModel(postDTO);
        post.setUser(user);
        post.setAClass(aClass);
        post.setCreatedAt(LocalDateTime.now());
        return postMapper.toDTO(postRepository.save(post));
    }

    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    public Page<PostDTO> getAllByClassPageable(Pageable pageable, Long classId) {
        Class classToFilter = classRepository.findById(classId)
                .orElseThrow(() -> new ClassNotFoundException(String.format("Não foi encontrada classe com o id:  %d", classId), "class.notFound"));

        User user = userService.findById(REQUEST_USER_ID).get();
        Pageable page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt"));

        return postRepository.findByaClass(classToFilter, page).map(p -> {
            PostDTO postDTO = postMapper.toDTO(p);
            if (p.getUsersFavorited().contains(user)) {
                postDTO.setFavorite(true);
            } else {
                postDTO.setFavorite(false);
            }
            return postDTO;
        });
    }

    public Page<PostDTO> getAllPageable(Pageable pageable, Long userId, Long classId) {
//        Class classToFilter = classRepository.findById(classId)
//                .orElseThrow(() -> new ClassNotFoundException(String.format("Não foi encontrada classe com o id:  %d", classId), "class.notFound"));

        User user = userService.findById(userId).get();
        Class aClass = classMapper.toModel(classService.getById(classId));
        return postRepository.findByaClassAndUser(aClass, user, pageable).map(postMapper::toDTO);
    }

    @Transactional
    public PostDTO favoritePost(Long postId, Long userId) {
        User user = userService.findById(userId).get();
        Post post = postRepository.findById(postId).get();
        post.getUsersFavorited().add(user);
        user.getFavoritesPosts().add(post);
        userService.update(user);

        post.setFavorite(true);
        return postMapper.toDTO(postRepository.save(post));
    }

    public PostDTO disfavorPost(Long postId, Long userId) {

        User user = userService.findById(userId).get();
        Post post = postRepository.findById(postId).get();
        post.getUsersFavorited().remove(user);
        user.getFavoritesPosts().remove(post);
        userService.update(user);

        post.setFavorite(false);
        return postMapper.toDTO(postRepository.save(post));
    }
}
