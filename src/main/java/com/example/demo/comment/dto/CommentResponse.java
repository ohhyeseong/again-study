package com.example.demo.comment.dto;

import com.example.demo.comment.domain.Comment;

public record CommentResponse(
        Long id,
        String content,
        String nickname,
        String title
) {
    public static CommentResponse from(Comment comment){
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getNickname(),
                comment.getPost().getTitle()
        );
    }
}
