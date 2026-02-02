package com.example.demo.user.dto;

public record TokenDto(
        String accessToken,
        String refreshToken
) {
}
