package com.woomoolmarket.redis;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisUtil {

    private final StringRedisTemplate stringRedisTemplate;

    public String getData(String key) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void setData(String key, Object value) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(key, String.valueOf(value));
    }

    public String getHashData(String key, String hashKey) {
        HashOperations<String, Object, Object> valueOperations = stringRedisTemplate.opsForHash();
        return (String) valueOperations.get(key, hashKey);
    }

    public void setHashData(String key, String hashKey, Object value) {
        HashOperations<String, String, String> valueOperations = stringRedisTemplate.opsForHash();
        valueOperations.put(key, hashKey, String.valueOf(value));
    }

    public void setDataExpire(String key, Object value, long duration) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, String.valueOf(value), expireDuration);
    }

    public void deleteData(String key) {
        stringRedisTemplate.delete(key);
    }
}
