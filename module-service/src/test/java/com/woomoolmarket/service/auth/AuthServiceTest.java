package com.woomoolmarket.service.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.woomoolmarket.config.ServiceTestConfig;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.security.dto.TokenRequest;
import com.woomoolmarket.security.dto.TokenResponse;
import com.woomoolmarket.service.member.dto.request.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

class AuthServiceTest extends ServiceTestConfig {

    private static final String RIGHT_PASSWORD = "123456";
    private static final String WRONG_PASSWORD = "wrong password";

    private static Member member;
    private static Member seller;

    @BeforeEach
    void init() {
        member = memberTestHelper.createUser();
        seller = memberTestHelper.createSeller();
    }

    private LoginRequest createRightLoginRequest() {
        return LoginRequest.builder()
            .email(member.getEmail())
            .password(RIGHT_PASSWORD)
            .build();
    }

    private LoginRequest createWrongLoginRequest() {
        return LoginRequest.builder()
            .email(member.getEmail())
            .password(WRONG_PASSWORD)
            .build();
    }

    @Test
    @DisplayName("로그인 성공")
    void login() {
        LoginRequest loginRequest = createRightLoginRequest();
        TokenResponse tokenResponse = authService.login(loginRequest);

        assertThat(tokenResponse).isNotNull();
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 틀릴 시 AuthenticationException 발생")
    void loginFail() {
        LoginRequest loginRequest = createWrongLoginRequest();

        assertThrows(AuthenticationException.class, () -> authService.login(loginRequest));
    }

    @Test
    @DisplayName("로그인 검증 성공")
    void authenticate() {
        UsernamePasswordAuthenticationToken token = createRightLoginRequest().toAuthentication();
        Authentication authentication = authService.authenticate(token);

        assertThat(authentication).isNotNull();
    }

    @Test
    @DisplayName("로그인 검증 실패 - 비밀번호 틀릴 시 AuthenticationException 발생")
    void authenticateFail() {
        UsernamePasswordAuthenticationToken token = createWrongLoginRequest().toAuthentication();

        assertThrows(AuthenticationException.class, () -> authService.authenticate(token));
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logout() {
        LoginRequest loginRequest = createRightLoginRequest();
        TokenResponse tokenResponse = authService.login(loginRequest);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + tokenResponse.getAccessToken());
        authService.logout(request);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNull();
    }

    @Test
    @DisplayName("로그아웃 실패 - AccessToken 틀릴 시 IllegalArgumentException 발생")
    void logoutFailByWrongAccessToken() {
        LoginRequest loginRequest = createRightLoginRequest();
        TokenResponse tokenResponse = authService.login(loginRequest);

        MockHttpServletRequest request = new MockHttpServletRequest();

        assertThrows(IllegalArgumentException.class, () -> authService.logout(request));
    }

    @Test
    @DisplayName("인증 정보 저장 - 로그인 성공 시 SecurityContextHolder authentication 저장")
    void setAuthenticationAfterLogin() {
        LoginRequest loginRequest = createRightLoginRequest();
        authService.login(loginRequest);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertThat(authentication).isNotNull();
    }

    @Test
    @DisplayName("인증 정보 유지 - 로그아웃 실패 시 인증 정보 초기화 되지 않음")
    void logoutFailByEmptyAuthorizationHeader() {
        LoginRequest loginRequest = createRightLoginRequest();
        authService.login(loginRequest);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer empty");
        assertThrows(IllegalArgumentException.class, () -> authService.logout(request));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
    }

    @Test
    @DisplayName("재발급 성공")
    void reissue() {
        LoginRequest loginRequest = createRightLoginRequest();
        TokenResponse tokenResponse = authService.login(loginRequest);

        TokenRequest tokenRequest = TokenRequest.builder()
            .accessToken(tokenResponse.getAccessToken())
            .refreshToken(tokenResponse.getRefreshToken())
            .build();
        TokenResponse reissuedTokenResponse = authService.reissue(tokenRequest);

        assertThat(reissuedTokenResponse).isNotNull();
    }

    @Test
    @DisplayName("재발급 실패 - Access 토큰 옳지 않을 시 NullPointerException 발생")
    void reissueFailByWrongAccessToken() {
        LoginRequest loginRequest = createRightLoginRequest();
        TokenResponse tokenResponse = authService.login(loginRequest);

        TokenRequest tokenRequest = TokenRequest.builder()
            .accessToken(tokenResponse.getRefreshToken())
            .refreshToken(tokenResponse.getRefreshToken())
            .build();
        assertThrows(NullPointerException.class, () -> authService.reissue(tokenRequest));
    }

    @Test
    @DisplayName("재발급 실패 - Refresh 토큰 옳지 않을 시 AccessDeniedException 발생")
    void reissueFailByWrongRefreshToken() {
        LoginRequest loginRequest = createRightLoginRequest();
        TokenResponse tokenResponse = authService.login(loginRequest);

        TokenRequest tokenRequest = TokenRequest.builder()
            .accessToken(tokenResponse.getAccessToken())
            .refreshToken(tokenResponse.getRefreshToken() + 1)
            .build();
        assertThrows(AccessDeniedException.class, () -> authService.reissue(tokenRequest));
    }
}