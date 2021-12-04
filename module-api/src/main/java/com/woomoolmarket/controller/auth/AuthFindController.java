package com.woomoolmarket.controller.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woomoolmarket.aop.annotation.LogExecutionTime;
import com.woomoolmarket.service.auth.AuthFindService;
import com.woomoolmarket.service.auth.dto.request.AuthStringRequest;
import com.woomoolmarket.service.auth.dto.request.EmailRequest;
import com.woomoolmarket.service.auth.dto.request.PhoneRequest;
import com.woomoolmarket.service.auth.dto.response.BalanceResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@LogExecutionTime
@RequiredArgsConstructor
@PreAuthorize("isAnonymous()")
@RequestMapping("/api/auth")
public class AuthFindController {

    private final ObjectMapper objectMapper;
    private final AuthFindService authFindService;

    @PostMapping("/email-verification")
    public ResponseEntity verifyEmail(
        @Valid @RequestBody EmailRequest emailRequest, BindingResult bindingResult) throws JsonProcessingException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(objectMapper.writeValueAsString(bindingResult));
        }

        authFindService.sendEmailForVerification(emailRequest.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth-string-verification")
    public ResponseEntity<Void> verifyAuthString(@RequestBody AuthStringRequest authStringRequest) {
        return authFindService.isVerified(authStringRequest.getAuthString()) ? ResponseEntity.ok().build()
            : ResponseEntity.badRequest().build();
    }

    @PostMapping("/id")
    public ResponseEntity findId(
        @Valid @RequestBody PhoneRequest phoneRequest, BindingResult bindingResult) throws JsonProcessingException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(objectMapper.writeValueAsString(bindingResult));
        }

        authFindService.sendAuthStringToPhone(phoneRequest.getPhone());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/pw")
    public ResponseEntity findPw(
        @Valid @RequestBody EmailRequest findRequest, BindingResult bindingResult) throws JsonProcessingException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(objectMapper.writeValueAsString(bindingResult));
        }

        authFindService.sendEmailForFinding(findRequest.getEmail());
        return ResponseEntity.ok().build();
    }

    // 잔고확인 ADMIN 만 가능하도록 설정
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/balance")
    public ResponseEntity<BalanceResponse> findBalance() {
        int balance = authFindService.checkBalance();
        BalanceResponse balanceResponse = BalanceResponse.builder()
            .balance(balance)
            .build();
        return ResponseEntity.ok(balanceResponse);
    }
}