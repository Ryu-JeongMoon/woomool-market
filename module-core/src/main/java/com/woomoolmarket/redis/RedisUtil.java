package com.woomoolmarket.redis;

import java.time.Duration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class RedisUtil {

    private final StringRedisTemplate stringRedisTemplate;
    private final ValueOperations<String, String> stringValueOperations;
    private final HashOperations<String, String, String> hashValueOperations;

    public RedisUtil(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.hashValueOperations = stringRedisTemplate.opsForHash();
        this.stringValueOperations = stringRedisTemplate.opsForValue();
    }

    public String getData(String key) {
        return stringValueOperations.get(key);
    }

    public void setData(String key, Object value) {
        stringValueOperations.set(key, String.valueOf(value));
    }

    public void setDataAndExpiration(String key, Object value, long duration) {
        Duration expireDuration = Duration.ofSeconds(duration);
        stringValueOperations.set(key, String.valueOf(value), expireDuration);
    }

    public void deleteData(String key) {
        stringRedisTemplate.delete(key);
    }

    public String getHashData(String key, String hashKey) {
        return hashValueOperations.get(key, hashKey);
    }

    public void setHashData(String key, String hashKey, Object value) {
        hashValueOperations.put(key, hashKey, String.valueOf(value));
    }
}
