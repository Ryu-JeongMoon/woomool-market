package com.woomoolmarket.controller.auth;

import com.woomoolmarket.aop.time.LogExecutionTime;
import com.woomoolmarket.security.dto.TokenRequest;
import com.woomoolmarket.security.dto.TokenResponse;
import com.woomoolmarket.service.auth.AuthService;
import com.woomoolmarket.service.member.dto.request.LoginRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@LogExecutionTime
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/api/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest,
        HttpServletResponse response) {
        TokenResponse tokenResponse = authService.login(loginRequest);
        Cookie cookie = prepareCookie(tokenResponse);
        response.addCookie(cookie);
        return ResponseEntity.ok(tokenResponse);
    }

    private Cookie prepareCookie(TokenResponse tokenResponse) {
        Cookie cookie = new Cookie("refreshToken", tokenResponse.getRefreshToken());
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        return cookie;
    }

    @PostMapping("/api/reissue")
    public ResponseEntity<TokenResponse> reissue(@RequestBody TokenRequest tokenRequest) {
        return ResponseEntity.ok(authService.reissue(tokenRequest));
    }
}

