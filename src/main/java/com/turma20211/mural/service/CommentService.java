package com.turma20211.mural.service;

import com.turma20211.mural.model.Comment;
import com.turma20211.mural.model.Post;
import com.turma20211.mural.model.User;
import com.turma20211.mural.repository.CommentRepository;
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

    public Comment makeComment(Long postId, Long userId, Comment comment){
        Optional<Post> post = postService.getById(postId);
        Optional<User> user = userService.findById(userId);

        if(post.isPresent() && user.isPresent()){
            comment.setPost(post.get());
            comment.setUser(user.get());
            return commentRepository.save(comment);
        }
        return new Comment();
    }
}
