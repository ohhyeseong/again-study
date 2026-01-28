package com.example.demo.comment.service;

import com.example.demo.comment.domain.Comment;
import com.example.demo.comment.dto.CommentResponse;
import com.example.demo.comment.dto.CommentCreateRequest;
import com.example.demo.comment.dto.CommentUpdateRequest;
import com.example.demo.comment.repository.CommentRepository;
import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.post.domain.Post;
import com.example.demo.post.repository.PostRepository;
import com.example.demo.user.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

        
        Comment parentComment = null;
        // dto 에서 대댓글을 입력할려고 하는 댓글 아이디(id번호)가 있으면 대댓글 저장 시키고 아니면 저장 안하게 할려는듯
        if(dto.parentId() != null) {
            // 부모가 있으면? 그 부모를(부모댓글) 찾아와.
            parentComment = commentRepository.findById(dto.parentId())
                    .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        }

        Comment comment = Comment.builder()
                .content(dto.content())
                .user(user)
                .post(post)
                .parent(parentComment)// 새로 만드는 댓글에 부모를(대댓글)을 설정해줘.
                .build();

        Comment response = commentRepository.save(comment);

        return CommentResponse.from(response);
    }

    public Page<CommentResponse> getComments(Long postId, Pageable pageable){
        return commentRepository.findAllByPostIdWithUserAndPost(postId, pageable)
                .map(CommentResponse::from);
    }

    @Transactional
    public CommentResponse update(@Valid CommentUpdateRequest dto, Long postId, Long commentId, User user){

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if(!comment.getUser().getId().equals(user.getId())){
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        comment.update(dto.content());

        return CommentResponse.from(comment);
    }

    @Transactional
    public void delete(Long postId, Long commentId, User user) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if(!comment.getUser().getId().equals(user.getId())){
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        commentRepository.delete(comment);
    }
}
