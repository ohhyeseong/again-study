package com.example.demo.user.service;

import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.jwt.JwtTokenProvider;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserRole;
import com.example.demo.user.dto.LoginRequest;
import com.example.demo.user.dto.UserResponse;
import com.example.demo.user.dto.UserSignupDto;
import com.example.demo.user.dto.UserUpdateRequestDto;
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
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public UserResponse signup(UserSignupDto dto) {

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

        User savedUser = userRepository.save(user);

        return UserResponse.from(savedUser);
    }

    public String login(LoginRequest dto) {
        // 1. 유저 확인
        User user = userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        // 3. 토큰 생성 빙 반환
        // user.getRole().name()은 "ROLE_USER" 같은 문자열이어야 합니다.
        return jwtTokenProvider.createToken(user.getUsername(), user.getRole().name());
    }

    public UserResponse findMyPage(Long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse update(Long userId, UserUpdateRequestDto dto){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.update(dto.nickname());

        return UserResponse.from(user);
    }
}
