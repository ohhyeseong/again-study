package com.example.demo.global.jwt;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    // AccessToken 재발습 시 RefreshToken으로 사용자를 찾기 위해 필요
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
