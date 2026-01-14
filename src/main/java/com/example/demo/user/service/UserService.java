package com.example.demo.user.service;

import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserRole;
import com.example.demo.user.dto.UserSignupDto;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signup(UserSignupDto dto) {

        if(userRepository.existsByUsername(dto.username())) {
            throw new CustomException(ErrorCode.CONFLICT_USERNAME);
        }

        String encodedPassword = passwordEncoder.encode(dto.password());

        User user = User.builder()
                .username(dto.username())
                .password(encodedPassword)
                .nickname(dto.nickname())
                .email(dto.email())
                .role(UserRole.USER)
                .build();

        return userRepository.save(user).getId();
    }
}
