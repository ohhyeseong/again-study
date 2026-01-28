package com.example.demo.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentUpdateRequest(

        @NotBlank(message = "필수 입련란 입니다.")
        @Size(max = 30,message = "최대 입력값은 30자 입니다.")
        String content
) {
}
