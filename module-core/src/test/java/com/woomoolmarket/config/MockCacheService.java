package com.woomoolmarket.config;

import com.woomoolmarket.cache.CacheService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("test")
@Component
public class MockCacheService implements CacheService {

  @Override
  public String getData(String key) {
    return null;
  }

  @Override
  public int getDataAsInt(String key) {
    return 0;
  }

  @Override
  public String getHashData(String key, String hashKey) {
    return null;
  }

  @Override
  public void setData(String key, Object value) {

  }

  @Override
  public void setHashData(String key, String hashKey, Object value) {

  }

  @Override
  public void setDataAndExpiration(String key, Object value, long duration) {

  }

  @Override
  public void deleteData(String key) {

  }

  @Override
  public void increment(String key) {

  }

  @Override
  public <T> T getObjectData(String key, Class<T> t) {
    return null;
  }

}
