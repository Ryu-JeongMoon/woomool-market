package com.woomoolmarket.security.jwt.factory;

import static org.assertj.core.api.Assertions.assertThat;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TokenTest {

  private static final String SECRET_KEY = "panda1panda2panda3panda4panda5panda6panda7panda8";
  private static Key KEY;

  static void printClaim(String key, Claims value) {
    if (value != null) {
      System.out.printf("%s:{map}%s\n", key, value);
      return;
    }
    System.out.println("====>> unknown type for :" + key);
  }

  @BeforeEach
  void keySetUp() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    KEY = Keys.hmacShaKeyFor(keyBytes);
  }

  @Test
  @DisplayName("토큰 생성")
  void createToken() {
    String token = Jwts.builder()
      .setIssuedAt(new Date())
      .setSubject("panda")
      .setHeader(Map.of("typ", "Bearer", "alg", "hs256"))
      .setExpiration(new Date(2021, 11, 31))
      .signWith(KEY, SignatureAlgorithm.HS256)
      .compact();

    assertThat(token).isNotNull();
  }

  @Test
  @DisplayName("type, alg 검증")
  void setHeader() {
    String token = Jwts.builder()
      .setIssuedAt(new Date())
      .setSubject("panda")
      .setHeader(Map.of("typ", "Bearer"))
      .setExpiration(new Date(2021, 11, 31))
      .signWith(KEY, SignatureAlgorithm.HS256)
      .compact();

    Header header = Jwts.parserBuilder()
      .setSigningKey(KEY)
      .build().parse(token).getHeader();

    String typ = header.getType();
    Object alg = header.get("alg");

    assertThat(typ).isEqualTo("Bearer");
    assertThat(alg).isEqualTo("HS256");
  }

  @Test
  @DisplayName("Claims 확인")
  void parseBody() {
    String token = Jwts.builder()
      .setIssuedAt(new Date())
      .setSubject("panda")
      .setHeader(Map.of("typ", "Bearer"))
      .setIssuedAt(new Date())
      .claim("exp", Instant.now().getEpochSecond() + 3)
      .signWith(KEY, SignatureAlgorithm.HS256)
      .compact();

    Claims body = (Claims) Jwts.parserBuilder()
      .setSigningKey(KEY)
      .build().parse(token).getBody();

    printClaim("alg", body);
  }
}