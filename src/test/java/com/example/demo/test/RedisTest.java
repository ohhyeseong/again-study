package com.example.demo.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void testRedis() {
        // given
        String Key = "testKey";
        String value = "hello redis";

        // when
        // opsForValue()는 String(Value) 타입의 연산을 도와줍니다.
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(Key, value);

        // then
        String result = (String) valueOperations.get(Key);
        System.out.println("가져온 값: " + result);

        assertThat(result).isEqualTo(value);
    }
}
