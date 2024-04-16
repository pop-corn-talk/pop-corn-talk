package com.popcorntalk.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.popcorntalk.global.entity.RefreshToken;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    public void set(String key, RefreshToken o, int minutes) throws JsonProcessingException {
        redisTemplate.opsForValue().set(key, mapper.writeValueAsString(o), minutes, TimeUnit.MINUTES);
    }

    public RefreshToken get(String key) throws JsonProcessingException {
        return mapper.readValue(redisTemplate.opsForValue().get(key),RefreshToken.class);
    }

    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}

