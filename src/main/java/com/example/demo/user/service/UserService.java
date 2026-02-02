package com.example.demo.user.service;

import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.jwt.JwtTokenProvider;
import com.example.demo.global.jwt.RefreshToken;
import com.example.demo.global.jwt.RefreshTokenRepository;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserRole;
import com.example.demo.user.dto.*;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public UserResponse signup(UserSignupDto dto) {

        if (userRepository.existsByUsername(dto.username())) {
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

    @Transactional
    public TokenDto login(LoginRequest dto) {
        // 1. 유저 확인
        User user = userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        // 2. 토큰 생성
        String accessToken = jwtTokenProvider.createToken(user.getUsername(), user.getRole().name());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUsername());

        // 3. RefreshToken Redis에 저장
        refreshTokenRepository.save(new RefreshToken(user.getUsername(), refreshToken));

        // 4. 토큰 반환
        return new TokenDto(accessToken, refreshToken);
    }

    public UserResponse findMyPage(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse update(Long userId, UserUpdateRequestDto dto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.update(dto.nickname());

        return UserResponse.from(user);
    }

    // refreshToken 확인 후 accessToken 재발급
    @Transactional
    public TokenDto reissue(RefreshTokenRequestDto dto) { // 반환 타입을 String으로 변경

        // 1. Refresh Token 검증
        String refreshTokenValue = dto.refreshToken();
        if (!jwtTokenProvider.validateToken(refreshTokenValue)) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND); // 적절한 에러 코드로 변경
        }

        // 2. Redis에서 Refresh Token 조회
        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(refreshTokenValue)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)); // 적절한 에러 코드로 변경

        // 3. DB에서 사용자 정보 조회
        User user = userRepository.findByUsername(refreshToken.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        refreshTokenRepository.delete(refreshToken);

        // 4. 새로운 Access Token 생성
        String newAccessToken = jwtTokenProvider.createToken(user.getUsername(), user.getRole().name());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(user.getUsername());

        refreshTokenRepository.save(new RefreshToken(user.getUsername(), newRefreshToken));

        // 5. 새 토큰 반환
        return new TokenDto(newAccessToken, newRefreshToken);
    }


}
