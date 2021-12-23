package com.woomoolmarket.service.cache;

import com.woomoolmarket.cache.CacheService;
import java.time.Duration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class RedisService implements CacheService {

    private final StringRedisTemplate stringRedisTemplate;
    private final ValueOperations<String, String> stringValueOperations;
    private final HashOperations<String, String, String> hashValueOperations;

    public RedisService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.hashValueOperations = stringRedisTemplate.opsForHash();
        this.stringValueOperations = stringRedisTemplate.opsForValue();
    }

    @Override
    public String getData(String key) {
        return stringValueOperations.get(key);
    }

    @Override
    public void setData(String key, Object value) {
        stringValueOperations.set(key, String.valueOf(value));
    }

    @Override
    public void increment(String key) {
        stringValueOperations.increment(key, 1L);
    }

    @Override
    public void setDataAndExpiration(String key, Object value, long duration) {
        Duration expireDuration = Duration.ofSeconds(duration);
        stringValueOperations.set(key, String.valueOf(value), expireDuration);
    }

    @Override
    public void deleteData(String key) {
        stringRedisTemplate.delete(key);
    }

    @Override
    public String getHashData(String key, String hashKey) {
        return hashValueOperations.get(key, hashKey);
    }

    @Override
    public void setHashData(String key, String hashKey, Object value) {
        hashValueOperations.put(key, hashKey, String.valueOf(value));
    }
}
