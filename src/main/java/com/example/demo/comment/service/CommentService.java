package com.example.demo.comment.service;

import com.example.demo.comment.domain.Comment;
import com.example.demo.comment.dto.CommentResponse;
import com.example.demo.comment.repository.CommentCreateRequest;
import com.example.demo.comment.repository.CommentRepository;
import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.post.domain.Post;
import com.example.demo.post.repository.PostRepository;
import com.example.demo.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentResponse create(CommentCreateRequest dto, User user, Long postId){

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        Comment comment = Comment.builder()
                .content(dto.content())
                .user(user)
                .post(post)
                .build();

        Comment response = commentRepository.save(comment);

        return CommentResponse.from(response);
    }

    public List<CommentResponse> getComments(Long postId){
        return commentRepository.findAllByPostIdWithUserAndPost(postId).stream()
                .map(CommentResponse::from)
                .toList();
    }
}
