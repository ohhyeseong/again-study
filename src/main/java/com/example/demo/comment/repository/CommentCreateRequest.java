package com.example.demo.comment.repository;

import jakarta.validation.constraints.NotBlank;

public record CommentCreateRequest(
        @NotBlank(message = "내용을 입력해주세요.")
        String content
){}
