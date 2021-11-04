package com.woomoolmarket.controller.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woomoolmarket.aop.time.LogExecutionTime;
import com.woomoolmarket.service.auth.AuthFindService;
import com.woomoolmarket.service.auth.dto.FindIdRequest;
import com.woomoolmarket.service.auth.dto.FindPasswordRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@LogExecutionTime
@RequiredArgsConstructor
@RequestMapping(value = "/api/auth")
public class AuthFindController {

    private final ObjectMapper objectMapper;
    private final AuthFindService authFindService;

    @PostMapping("/id")
    public ResponseEntity findId(
        @Valid @RequestBody FindIdRequest findIdRequest, BindingResult bindingResult) throws JsonProcessingException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(objectMapper.writeValueAsString(bindingResult));
        }

        authFindService.sendAuthStringToPhone(findIdRequest.getPhone());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/pw")
    public ResponseEntity findPw(
        @Valid @RequestBody FindPasswordRequest findRequest, BindingResult bindingResult) throws JsonProcessingException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(objectMapper.writeValueAsString(bindingResult));
        }

        authFindService.sendAuthStringToEmail(findRequest.getEmail());
        return ResponseEntity.ok().build();
    }
}
