package com.example.demo.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostUpdateDto(

        @NotBlank(message = "제목을 입력해주세요")
        String title,

        @NotBlank(message = "내용을 입력해주세요")
        @Size(max = 200,message = "최대 200자 이내로 입력해주세요")
        String content

) {
}
