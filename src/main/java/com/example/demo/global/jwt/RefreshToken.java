package com.example.demo.global.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@AllArgsConstructor
// JPA의 @Entity와 비슷한 역할, Redis에 저장될 객체임을 명시한다.
@RedisHash(value = "refreshToken", timeToLive = 60 * 60 * 24 * 3)// 3일 동안 유지 (초 단위)
public class RefreshToken {

    @Id
    private String id; // Redis Key (보통 사용자 ID나 이메일)

    @Indexed // 찾기 쉽게 인덱심
    private String refreshToken; // 이 필드 값으로 데이터를 검색(findByRefreshToken) 할 수 있게 해줌.

    // 필요하다면 권한 정보 등 추가 필드
}
