package com.example.demo.post.dto;

import com.example.demo.post.domain.Post;

public record PostResponse(
        Long id,
        String title,
        String content,
        String nickname
) {
    public static PostResponse from(Post post){
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getNickname()
        );
    }
}
