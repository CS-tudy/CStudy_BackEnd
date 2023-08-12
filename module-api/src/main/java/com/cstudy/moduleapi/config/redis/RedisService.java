package com.cstudy.moduleapi.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public void setValues(String token) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(token, token, Duration.ofDays(7));
    }

    public String getValues(String token) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(token);
    }

    public void delValues(String token) {
        redisTemplate.delete(token);
    }
}