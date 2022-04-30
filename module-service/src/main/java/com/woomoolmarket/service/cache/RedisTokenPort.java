package com.woomoolmarket.service.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woomoolmarket.domain.port.CacheTokenPort;
import java.time.Duration;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RedisTokenPort implements CacheTokenPort {

  private final ObjectMapper objectMapper;
  private final StringRedisTemplate stringRedisTemplate;
  private final ValueOperations<String, String> stringValueOperations;
  private final HashOperations<String, String, String> hashValueOperations;

  public RedisTokenPort(ObjectMapper objectMapper, StringRedisTemplate stringRedisTemplate) {
    this.objectMapper = objectMapper;
    this.stringRedisTemplate = stringRedisTemplate;
    this.hashValueOperations = stringRedisTemplate.opsForHash();
    this.stringValueOperations = stringRedisTemplate.opsForValue();
  }

  @Override
  public String getData(String key) {
    return stringValueOperations.get(key);
  }

  @Override
  public int getDataAsInt(String key) {
    return Integer.parseInt(Objects.requireNonNull(stringValueOperations.get(key)));
  }

  @Override
  public void setData(String key, Object value) {
    stringValueOperations.set(key, String.valueOf(value));
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

  @Override
  public void increment(String key) {
    stringValueOperations.increment(key, 1L);
  }

  @Override
  public <T> T getObjectData(String key, Class<T> t) {
    try {
      return objectMapper.readValue(stringValueOperations.get(key), new TypeReference<>() {
      });
    } catch (JsonProcessingException e) {
      log.info("[WOOMOOL-ERROR] :: Invalid Cache Key => {} ", e.getMessage());
      return null;
    }
  }
}
