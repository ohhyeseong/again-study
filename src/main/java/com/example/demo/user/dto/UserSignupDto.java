package com.example.demo.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserSignupDto(

        @NotBlank(message = "아이디는 필수 입력값입니다.")
        String username,

        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        String password,

        @NotBlank(message = "닉네임은 필수 입력값입니다.")
        String nickname,

        @NotBlank(message = "이메일은 필수 입력값입니다.")
        @Email(message = "이메일 형식에 맞지 않습니다.")
        String email
) {
}
