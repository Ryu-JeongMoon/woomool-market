package com.woomoolmarket.domain.port;

public interface CacheTokenPort {

  String getData(String key);

  int getDataAsInt(String key);

  String getHashData(String key, String hashKey);

  void setData(String key, Object value);

  void setHashData(String key, String hashKey, Object value);

  void setDataAndExpiration(String key, Object value, long duration);

  void deleteData(String key);

  void increment(String key);

  <T> T getObjectData(String key, Class<T> t);
}
