package com.woomoolmarket.cache.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woomoolmarket.domain.embeddable.Address;
import com.woomoolmarket.domain.member.entity.Authority;
import com.woomoolmarket.domain.member.entity.Member;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

@Log4j2
class CacheConfigTest {

  ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void cacheTest() throws JsonProcessingException {
    Member panda = Member.builder()
      .password("1234")
      .address(new Address("seoul", "jeonju", "busan"))
      .authority(Authority.ROLE_USER)
      .build();

    String str = objectMapper.writeValueAsString(panda);
    Object obj = objectMapper.readValue(str, Object.class);
    log.info("str = {}", str);
    log.info("obj = {}", obj);

  }
}