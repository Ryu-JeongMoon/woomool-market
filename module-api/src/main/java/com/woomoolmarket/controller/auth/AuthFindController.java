package com.woomoolmarket.controller.auth;

import com.woomoolmarket.service.auth.AuthFindService;
import com.woomoolmarket.service.auth.dto.request.AuthStringRequest;
import com.woomoolmarket.service.auth.dto.request.EmailRequest;
import com.woomoolmarket.service.auth.dto.request.PhoneRequest;
import com.woomoolmarket.service.auth.dto.response.BalanceResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthFindController {

    private final AuthFindService authFindService;

    @PostMapping("/email-verification")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<Void> verifyEmail(@Valid @RequestBody EmailRequest emailRequest) {
        authFindService.sendEmailForVerification(emailRequest.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth-string-verification")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<Void> verifyAuthString(@RequestBody AuthStringRequest authStringRequest) {
        return authFindService.isVerified(authStringRequest.getAuthString()) ? ResponseEntity.ok().build()
            : ResponseEntity.badRequest().build();
    }

    @PostMapping("/id")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<Void> findId(@Valid @RequestBody PhoneRequest phoneRequest) {
        authFindService.sendAuthStringToPhone(phoneRequest.getPhone());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/pw")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<Void> findPw(@Valid @RequestBody EmailRequest findRequest) {
        authFindService.sendEmailForFinding(findRequest.getEmail());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/balance")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<BalanceResponse> findBalance() {
        int balance = authFindService.checkBalance();
        BalanceResponse balanceResponse = BalanceResponse.builder()
            .balance(balance)
            .build();
        return ResponseEntity.ok(balanceResponse);
    }
}