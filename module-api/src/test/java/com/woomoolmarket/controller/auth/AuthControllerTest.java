package com.woomoolmarket.controller.auth;

import static com.woomoolmarket.helper.MemberTestHelper.MEMBER_EMAIL;
import static com.woomoolmarket.helper.MemberTestHelper.MEMBER_PASSWORD;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woomoolmarket.config.ApiControllerConfig;
import com.woomoolmarket.security.dto.TokenRequest;
import com.woomoolmarket.security.dto.TokenResponse;
import com.woomoolmarket.service.auth.AuthService;
import com.woomoolmarket.service.member.dto.request.LoginRequest;
import java.util.Objects;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.util.NestedServletException;

class AuthControllerTest extends ApiControllerConfig {

  @Autowired
  private AuthService authService;

  @BeforeEach
  void init() {
    MEMBER_ID = memberTestHelper.createMember().getId();
  }

  @AfterEach
  void clear() {
    SecurityContextHolder.clearContext();
    Objects.requireNonNull(stringRedisTemplate.keys("*")).forEach(k -> stringRedisTemplate.delete(k));
  }

  @Test
  @WithAnonymousUser
  @DisplayName("로그인 성공")
  void login() throws Exception {
    LoginRequest loginRequest = LoginRequest.builder()
      .email(MEMBER_EMAIL)
      .password(MEMBER_PASSWORD)
      .build();

    mockMvc.perform(
        post("/api/auth/login")
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaType.ALL)
          .content(objectMapper.writeValueAsString(loginRequest)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("grantType").exists())
      .andExpect(jsonPath("accessToken").exists())
      .andExpect(jsonPath("refreshToken").exists())
      .andExpect(jsonPath("accessTokenExpiresIn").exists());
  }

  @Test
  @WithAnonymousUser
  @DisplayName("로그인 실패 - 400 @Valid 작동")
  void loginFail() throws Exception {
    LoginRequest loginRequest = LoginRequest.builder()
      .email("fail")
      .password(MEMBER_PASSWORD)
      .build();

    mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.ALL)
        .content(objectMapper.writeValueAsString(loginRequest)))
      .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(username = "panda@naver.com", roles = "USER")
  @DisplayName("로그아웃 성공")
  void logout() throws Exception {
    LoginRequest loginRequest = LoginRequest.builder()
      .email(MEMBER_EMAIL)
      .password(MEMBER_PASSWORD)
      .build();

    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = loginRequest.toAuthenticationToken();
    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

    TokenResponse tokenResponse = authService.login(loginRequest);

    mockMvc.perform(
        post("/api/auth/logout")
          .with(request -> {
            request.addHeader("Authorization", "Bearer " + tokenResponse.getAccessToken());
            return request;
          })
          .accept(MediaType.ALL))
      .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("로그아웃 실패 - 400 잘못된 access token")
  void logoutFailByAccessToken() throws Exception {
    LoginRequest loginRequest = LoginRequest.builder()
      .email(MEMBER_EMAIL)
      .password(MEMBER_PASSWORD)
      .build();

    TokenResponse tokenResponse = authService.login(loginRequest);

    mockMvc.perform(
        post("/api/auth/logout")
          .with(request -> {
            request.addHeader("Authorization", "Bearer" + tokenResponse.getAccessToken());
            return request;
          })
          .accept(MediaType.ALL))
      .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("로그아웃 실패 - 401 중복 로그아웃 시도 AuthenticationCredentialsNotFoundException 발생")
  void logoutFailByDuplicateLogout() throws Exception {
    LoginRequest loginRequest = LoginRequest.builder()
      .email(MEMBER_EMAIL)
      .password(MEMBER_PASSWORD)
      .build();

    TokenResponse tokenResponse = authService.login(loginRequest);
    MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
    mockHttpServletRequest.addHeader("Authorization", "Bearer " + tokenResponse.getAccessToken());
    authService.logout(mockHttpServletRequest);

    mockMvc.perform(
        post("/api/auth/logout")
          .with(request -> {
            request.addHeader("Authorization", "Bearer " + tokenResponse.getAccessToken());
            return request;
          })
          .accept(MediaType.ALL))
      .andExpect(status().isUnauthorized())
      .andExpect(jsonPath("exception").value("AuthenticationCredentialsNotFoundException"));
  }

  @Test
  @DisplayName("토큰 재발급 성공")
  void reissue() throws Exception {
    LoginRequest loginRequest = LoginRequest.builder()
      .email(MEMBER_EMAIL)
      .password(MEMBER_PASSWORD)
      .build();
    TokenResponse tokenResponse = authService.login(loginRequest);
    TokenRequest tokenRequest = TokenRequest.builder()
      .accessToken(tokenResponse.getAccessToken())
      .refreshToken(tokenResponse.getRefreshToken())
      .build();

    mockMvc.perform(
        post("/api/auth/reissue")
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .content(objectMapper.writeValueAsString(tokenRequest))
          .accept(MediaType.ALL))
      .andExpect(status().isOk())
      .andExpect(jsonPath("grantType").exists())
      .andExpect(jsonPath("accessToken").exists())
      .andExpect(jsonPath("refreshToken").exists())
      .andExpect(jsonPath("accessTokenExpiresIn").exists());
  }

  @Test
  @DisplayName("토큰 재발급 실패 - 잘못된 토큰 NestedServletException -> SignatureException 발생")
  void reissueFailByWrongToken() {
    LoginRequest loginRequest = LoginRequest.builder()
      .email(MEMBER_EMAIL)
      .password(MEMBER_PASSWORD)
      .build();
    TokenResponse tokenResponse = authService.login(loginRequest);
    TokenRequest tokenRequest = TokenRequest.builder()
      .accessToken(tokenResponse.getAccessToken() + 1)
      .refreshToken(tokenResponse.getRefreshToken() + 1)
      .build();

    assertThrows(
      NestedServletException.class, () -> mockMvc.perform(
          post("/api/auth/reissue")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(tokenRequest))
            .accept(MediaType.ALL))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("exception").value("NestedServletException")));
  }
}