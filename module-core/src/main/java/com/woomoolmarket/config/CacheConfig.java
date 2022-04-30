package com.woomoolmarket.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.woomoolmarket.util.errors.JsonBindingResultModule;
import java.time.Duration;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class CacheConfig {

  private static final int DEFAULT_EXPIRE_TIME = 10;

  private final ObjectMapper objectMapper;
  private final RedisConnectionFactory redisConnectionFactory;

  @Bean(name = "jdkCacheManager")
  public RedisCacheManager jdkCacheManager() {
    RedisCacheConfiguration configuration = RedisCacheConfiguration
      .defaultCacheConfig()
      .disableCachingNullValues()
      .disableKeyPrefix()
      .entryTtl(Duration.ofDays(1))
      .serializeKeysWith(SerializationPair.fromSerializer(new StringRedisSerializer()))
      .serializeValuesWith(SerializationPair.fromSerializer(new JdkSerializationRedisSerializer()));

    Map<String, RedisCacheConfiguration> cacheConfigurations = setCustomCacheConfigurations(configuration);

    return RedisCacheManagerBuilder
      .fromConnectionFactory(redisConnectionFactory)
      .cacheDefaults(configuration)
      .withInitialCacheConfigurations(cacheConfigurations)
      .build();
  }

  @Primary
  @Bean(name = "gsonCacheManager")
  public RedisCacheManager gsonCacheManager() {
    RedisCacheConfiguration configuration = RedisCacheConfiguration
      .defaultCacheConfig()
      .disableCachingNullValues()
      .entryTtl(Duration.ofSeconds(DEFAULT_EXPIRE_TIME))
      .serializeKeysWith(SerializationPair.fromSerializer(new StringRedisSerializer()))
      .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(customObjectMapper())));

    Map<String, RedisCacheConfiguration> cacheConfigurations = setCustomCacheConfigurations(configuration);

    return RedisCacheManagerBuilder
      .fromConnectionFactory(redisConnectionFactory)
      .cacheDefaults(configuration)
      .withInitialCacheConfigurations(cacheConfigurations)
      .build();
  }

  @Bean(name = "jsonCacheManager")
  public RedisCacheManager jsonCacheManager() {
    ObjectMapper objectMapper = new ObjectMapper()
      .findAndRegisterModules()
      .registerModule(new ParameterNamesModule())
      .enable(SerializationFeature.INDENT_OUTPUT)
      .enable(Feature.WRITE_BIGDECIMAL_AS_PLAIN)
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .setVisibility(PropertyAccessor.ALL, Visibility.ANY)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .enableDefaultTypingAsProperty(DefaultTyping.NON_FINAL, "@class");

    Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
    jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

    RedisCacheConfiguration configuration = RedisCacheConfiguration
      .defaultCacheConfig()
      .disableCachingNullValues()
      .disableKeyPrefix()
      .entryTtl(Duration.ofDays(DEFAULT_EXPIRE_TIME))
      .serializeValuesWith(SerializationPair.fromSerializer(new StringRedisSerializer()))
      .serializeValuesWith(SerializationPair.fromSerializer(jackson2JsonRedisSerializer));

    Map<String, RedisCacheConfiguration> cacheConfigurations = setCustomCacheConfigurations(configuration);

    return RedisCacheManagerBuilder
      .fromConnectionFactory(redisConnectionFactory)
      .cacheDefaults(configuration)
      .withInitialCacheConfigurations(cacheConfigurations)
      .build();
  }

  private Map<String, RedisCacheConfiguration> setCustomCacheConfigurations(RedisCacheConfiguration configuration) {
    return Map.of(
      "brief", configuration.entryTtl(Duration.ofSeconds(10)),
      "medium", configuration.entryTtl(Duration.ofMinutes(1)),
      "long", configuration.entryTtl(Duration.ofMinutes(10)),
      "BoardService::boards", configuration.entryTtl(Duration.ofMinutes(5)),
      "BoardService::boardsForAdmin", configuration.entryTtl(Duration.ofMinutes(5))
    );
  }

  private ObjectMapper customObjectMapper() {
    PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
      .allowIfSubType(Object.class)
      .build();

    return JsonMapper.builder()
      .polymorphicTypeValidator(ptv)
      .findAndAddModules()
      .enable(SerializationFeature.INDENT_OUTPUT)
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .addModules(new JavaTimeModule(), new JsonBindingResultModule(), new DateTimeFormatModule())
      .activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL)
      .build();
  }
}