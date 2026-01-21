package com.example.demo.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequestDto(
        @NotBlank(message = "닉네임은 필수 입력값입니다.")
        @Size(max = 18, message = "최대 18자 까지 가능합니다.")
        @Size(min = 1, message ="최소 1글자 이상은 작성해야 합니다.")
        String nickname
) {
}
