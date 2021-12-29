package com.woomoolmarket.controller.auth;

import static com.woomoolmarket.helper.MemberTestHelper.MEMBER_EMAIL;
import static com.woomoolmarket.helper.MemberTestHelper.MEMBER_PHONE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woomoolmarket.config.ApiControllerConfig;
import com.woomoolmarket.service.auth.dto.request.AuthStringRequest;
import com.woomoolmarket.service.auth.dto.request.EmailRequest;
import com.woomoolmarket.service.auth.dto.request.PhoneRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

@WithAnonymousUser
class AuthFindControllerTest extends ApiControllerConfig {

    @BeforeEach
    void init() {
        memberTestHelper.createMember();
    }

    @Test
    @DisplayName("이메일 전송 성공")
    void verifyEmail() throws Exception {
        EmailRequest emailRequest = EmailRequest.builder()
            .email(MEMBER_EMAIL)
            .build();

        mockMvc.perform(
                post("/api/auth/email-verification")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(emailRequest))
                    .characterEncoding("UTF8"))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("이메일 전송 실패 - 400 잘못된 이메일 형식")
    void verifyEmailFailByWrongEmail() throws Exception {
        EmailRequest emailRequest = EmailRequest.builder()
            .email("panda!!")
            .build();

        mockMvc.perform(
                post("/api/auth/email-verification")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(emailRequest))
                    .characterEncoding("UTF8"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("전송된 문자열 확인 실패 - 400 잘못된 문자열")
    void verifyAuthStringFailByWrongAuthString() throws Exception {
        AuthStringRequest authStringRequest = AuthStringRequest.builder()
            .authString("panda")
            .build();

        mockMvc.perform(
                post("/api/auth/auth-string-verification")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(authStringRequest))
                    .accept(MediaType.ALL)
                    .characterEncoding("UTF8"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("메세지 전송 성공")
    void findId() throws Exception {
        PhoneRequest phoneRequest = PhoneRequest.builder()
            .phone(MEMBER_PHONE)
            .build();

        mockMvc.perform(
                post("/api/auth/id")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(phoneRequest))
                    .accept(MediaType.ALL)
                    .characterEncoding("UTF8"))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("메세지 전송 실패 - 잘못된 전화번호 형식")
    void findIdFailByValidation() throws Exception {
        PhoneRequest phoneRequest = PhoneRequest.builder()
            .phone("010")
            .build();

        mockMvc.perform(
                post("/api/auth/id")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(phoneRequest))
                    .accept(MediaType.ALL)
                    .characterEncoding("UTF8"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("새로운 비밀번호 이메일 전송 성공")
    void findPw() throws Exception {
        EmailRequest emailRequest = EmailRequest.builder()
            .email(MEMBER_EMAIL)
            .build();

        mockMvc.perform(
                post("/api/auth/pw")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(emailRequest))
                    .accept(MediaType.ALL)
                    .characterEncoding("UTF8"))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("새로운 비밀번호 이메일 전송 실패 - 400 존재하지 않는 이메일")
    void findPwFailByWrongEmail() throws Exception {
        EmailRequest emailRequest = EmailRequest.builder()
            .email(MEMBER_EMAIL + 1)
            .build();

        mockMvc.perform(
                post("/api/auth/pw")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(emailRequest))
                    .accept(MediaType.ALL)
                    .characterEncoding("UTF8"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("coolsms 잔액 확인 성공")
    void findBalance() throws Exception {
        mockMvc.perform(
                get("/api/auth/balance")
                    .accept(MediaType.ALL)
                    .characterEncoding("UTF8"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("balance").exists());
    }

    @Test
    @WithMockUser(roles = "SELLER")
    @DisplayName("coolsms 잔액 확인 실패 - 403 권한 없음")
    void findBalanceFailByNoAuthority() throws Exception {
        mockMvc.perform(
                get("/api/auth/balance")
                    .accept(MediaType.ALL)
                    .characterEncoding("UTF8"))
            .andExpect(status().isForbidden());
    }
}