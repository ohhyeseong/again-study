package com.example.demo.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentCreateRequest(
        @NotBlank(message = "내용을 입력해주세요.")
        @Size(max = 30,message = "최대 입력값은 30자 입니다.")
        String content,
        Long parentId
){}
