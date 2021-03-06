package com.woomoolmarket.security.jwt;

import static java.lang.String.format;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woomoolmarket.ModuleApiApplication;
import com.woomoolmarket.domain.entity.enumeration.Role;
import com.woomoolmarket.util.constants.TokenConstants;
import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.repository.MemberRepository;
import com.woomoolmarket.service.member.dto.request.LoginRequest;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(classes = ModuleApiApplication.class)
@AutoConfigureMockMvc
class JwtAuthenticationFilterTest {

  private final PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();
  private final String EMAIL = "panda@naver.com";
  private final String NICKNAME = "panda";
  private final String PASSWORD = "123456";

  @Autowired
  MockMvc mockMvc;
  @Autowired
  ObjectMapper mapper;
  @Autowired
  MemberRepository memberRepository;

  private URI uri(String path) throws URISyntaxException {
    return new URI(format("https://localhost:8443%s", path));
  }

  @BeforeEach
  void init() {
    Member member = Member.builder()
      .email(EMAIL)
      .nickname(NICKNAME)
      .password(passwordEncoder.encode(PASSWORD))
      .role(Role.USER)
      .build();
    memberRepository.save(member);
  }

  @Test
  @DisplayName("login ?????? ??? token-response ????????????")
  void doFilter() throws Exception {
    LoginRequest loginRequest = LoginRequest.builder()
      .email(EMAIL)
      .password(PASSWORD)
      .build();

    mockMvc.perform(
        post(uri("/api/auth/login"))
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(loginRequest)))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("grantType").value(TokenConstants.BEARER_TYPE))
      .andExpect(jsonPath("accessToken").exists())
      .andExpect(jsonPath("refreshToken").exists())
      .andExpect(jsonPath("accessTokenExpiresIn").exists());
  }
}