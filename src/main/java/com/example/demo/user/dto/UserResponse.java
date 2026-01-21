package com.example.demo.user.dto;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserRole;

public record UserResponse(
        Long id,
        String username,
        String nickname,
        String email,
        UserRole role
) {

    public static UserResponse from(User user){
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getEmail(),
                user.getRole()
        );
    }
}
