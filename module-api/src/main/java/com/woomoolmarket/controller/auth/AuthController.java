package com.woomoolmarket.controller.auth;

import com.woomoolmarket.common.util.CookieUtils;
import com.woomoolmarket.security.dto.TokenRequest;
import com.woomoolmarket.security.dto.TokenResponse;
import com.woomoolmarket.common.constants.TokenConstants;
import com.woomoolmarket.service.auth.AuthService;
import com.woomoolmarket.service.member.dto.request.LoginRequest;
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
        CookieUtils.addCookie(
            response, TokenConstants.REFRESH_TOKEN, tokenResponse.getRefreshToken(), TokenConstants.REFRESH_TOKEN_EXPIRE_SECONDS);
        response.setHeader(TokenConstants.AUTHORIZATION_HEADER, tokenResponse.getAccessToken());
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
        return ResponseEntity.ok(authService.reissue(tokenRequest));
    }
}