package com.example.demo.comment.dto;

import com.example.demo.comment.domain.Comment;

import java.util.List;
import java.util.stream.Collectors;

public record CommentResponse(
        Long id,
        String content,
        String nickname,
        String title,
        List<CommentResponse> children // 내 자식(대댓글) 의 정보
) {
    public static CommentResponse from(Comment comment){
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getNickname(),
                comment.getPost().getTitle(),
                comment.getChildren().stream()
                        .map(CommentResponse::from)
                        .collect(Collectors.toList())
        );
    }
}
