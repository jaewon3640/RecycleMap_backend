package com.example.RecycleProject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class RedisConnectionTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void connectionTest() {
        redisTemplate.opsForValue().set("test", "hello");
        Object value = redisTemplate.opsForValue().get("test");
        assertThat(value).isEqualTo("hello");
    }
}
