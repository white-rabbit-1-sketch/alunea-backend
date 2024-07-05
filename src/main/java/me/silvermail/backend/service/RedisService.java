package me.silvermail.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void remove(String key) {
        redisTemplate.delete(key);
    }

    public void increment(String key, int delta, Duration ttl) {
        redisTemplate.opsForValue().increment(key, delta);
        redisTemplate.expire(key, ttl);
    }

    public int getCounter(String key) {
        Object value = redisTemplate.opsForValue().get(key);

        return value != null ? (int) value : 0;
    }
}
