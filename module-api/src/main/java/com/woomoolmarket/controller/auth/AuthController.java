package com.woomoolmarket.controller.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woomoolmarket.aop.time.LogExecutionTime;
import com.woomoolmarket.security.dto.TokenRequest;
import com.woomoolmarket.security.dto.TokenResponse;
import com.woomoolmarket.service.auth.AuthService;
import com.woomoolmarket.service.member.dto.request.LoginRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@LogExecutionTime
@RequiredArgsConstructor
@RequestMapping(value = "/api/auth")
public class AuthController {

    private final AuthService authService;
    private final ObjectMapper objectMapper;

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult,
        HttpServletResponse response) throws JsonProcessingException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(objectMapper.writeValueAsString(bindingResult));
        }

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
        cookie.setDomain("woomool-market");
        return cookie;
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        authService.logout(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(@RequestBody TokenRequest tokenRequest) {
        return ResponseEntity.ok(authService.reissue(tokenRequest));
    }
}