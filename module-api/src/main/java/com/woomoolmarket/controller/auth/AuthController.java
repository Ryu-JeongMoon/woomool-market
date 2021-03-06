package com.woomoolmarket.controller.auth;

import static com.woomoolmarket.util.constants.TokenConstants.AUTHORIZATION_HEADER;
import static com.woomoolmarket.util.constants.TokenConstants.REFRESH_TOKEN;

import com.woomoolmarket.security.dto.TokenRequest;
import com.woomoolmarket.security.dto.TokenResponse;
import com.woomoolmarket.service.auth.AuthService;
import com.woomoolmarket.service.member.dto.request.LoginRequest;
import com.woomoolmarket.util.CookieUtils;
import com.woomoolmarket.util.constants.Times;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  @PreAuthorize("isAnonymous()")
  public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
    TokenResponse tokenResponse = authService.login(loginRequest);
    CookieUtils.addCookie(response, REFRESH_TOKEN, tokenResponse.getRefreshToken(), Times.REFRESH_TOKEN_EXPIRATION_SECONDS.getValue());
    response.setHeader(AUTHORIZATION_HEADER, tokenResponse.getAccessToken());
    return ResponseEntity.ok(tokenResponse);
  }

  @PostMapping("/logout")
  @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_SELLER', 'ROLE_ADMIN')")
  public ResponseEntity<Void> logout(HttpServletRequest request) {
    authService.logout(request);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/reissue")
  @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_SELLER', 'ROLE_ADMIN')")
  public ResponseEntity<TokenResponse> reissue(@RequestBody TokenRequest tokenRequest) {
    TokenResponse tokenResponse = authService.reissue(tokenRequest);
    return ResponseEntity.ok(tokenResponse);
  }
}