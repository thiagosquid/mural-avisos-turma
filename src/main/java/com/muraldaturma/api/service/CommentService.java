package com.muraldaturma.api.service;

import com.muraldaturma.api.exception.UserNotFoundException;
import com.muraldaturma.api.model.User;
import com.muraldaturma.api.repository.CommentRepository;
import com.muraldaturma.api.model.Comment;
import com.muraldaturma.api.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    public Comment makeComment(Long postId, Long userId, Comment comment) throws UserNotFoundException {
        Optional<Post> post = postService.getById(postId);
        Optional<User> user = userService.findById(userId);

        if(post.isPresent() && user.isPresent()){
            comment.setPost(post.get());
            comment.setUser(user.get());
            return commentRepository.save(comment);
        }
        return new Comment();
    }

    public void delete(Long id){
        commentRepository.deleteById(id);
    }
}
