package com.woomoolmarket.controller.member;

import static com.woomoolmarket.helper.MemberTestHelper.MEMBER_EMAIL;
import static com.woomoolmarket.helper.MemberTestHelper.MEMBER_PASSWORD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woomoolmarket.config.ApiControllerConfig;
import com.woomoolmarket.domain.member.entity.Address;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.service.member.dto.request.LoginRequest;
import com.woomoolmarket.service.member.dto.request.ModifyRequest;
import com.woomoolmarket.service.member.dto.request.SignUpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

class MemberControllerTest extends ApiControllerConfig {

    @BeforeEach
    void init() {
        Member member = memberTestHelper.createUser();
        MEMBER_ID = member.getId();
    }

    @Test
    @DisplayName("회원가입 성공")
    void signUpSuccessTest() throws Exception {
        SignUpRequest signUpRequest = SignUpRequest.builder()
            .email("pandabear@gogo.com")
            .nickname("nick")
            .password("123456")
            .address(new Address("seoul", "yeonhui", "1234"))
            .build();

        mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.ALL)
                .content(objectMapper.writeValueAsString(signUpRequest)))
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("회원가입 실패 - 400 @Valid 동작")
    void signUpFailTest() throws Exception {
        SignUpRequest signUpRequest = SignUpRequest.builder()
            .email("pandabear@gogo.com")
            .nickname("nick")
            .password("123")
            .address(new Address("seoul", "yeonhui", "1234"))
            .build();

        mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("로그인 성공")
    void loginSuccessTest() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
            .email(MEMBER_EMAIL)
            .password(MEMBER_PASSWORD)
            .build();

        mockMvc.perform(post("/api/auth/login")
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
    void loginFailTest() throws Exception {
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
    @DisplayName("회원 조회 성공 - @PreAuthorize 통과")
    void getMember() throws Exception {
        mockMvc.perform(
                get("/api/members/" + MEMBER_ID)
                    .accept(MediaType.ALL))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaTypes.HAL_JSON))
            .andExpect(jsonPath("id").value(MEMBER_ID))
            .andExpect(jsonPath("email").value(MEMBER_EMAIL))
            .andExpect(jsonPath("status").exists())
            .andExpect(jsonPath("createdDateTime").exists())
            .andExpect(jsonPath("lastModifiedDateTime").exists());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("회원 조회 실패 - 404 존재하지 않는 회원")
    void getMemberFail() throws Exception {
        mockMvc.perform(
                get("/api/members/" + 1L)
                    .accept(MediaType.ALL))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "panda@naver.com", roles = "USER")
    @DisplayName("수정 성공")
    void edit() throws Exception {
        ModifyRequest modifyRequest = ModifyRequest.builder()
            .nickname("panda")
            .phone("01012345678")
            .build();

        mockMvc.perform(
                patch("/api/members/" + MEMBER_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(modifyRequest)))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "panda@naver.com", roles = "USER")
    @DisplayName("수정 실패 - 400 @Valid 동작")
    void editFail() throws Exception {
        ModifyRequest modifyRequest = ModifyRequest.builder()
            .nickname("panda")
            .phone("0101234")
            .build();

        mockMvc.perform(
                patch("/api/members/" + MEMBER_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(modifyRequest)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "panda@naver.com", roles = "USER")
    @DisplayName("탈퇴 성공")
    void leave() throws Exception {
        mockMvc.perform(
                delete("/api/members/" + MEMBER_ID))
            .andDo(print())
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "mock", roles = "USER")
    @DisplayName("탈퇴 실패 - 403 @checker 동작")
    void leaveFail() throws Exception {
        mockMvc.perform(
                delete("/api/members/" + MEMBER_ID))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("관리자 단건 조회 성공")
    void getOneForAdmin() throws Exception {
        mockMvc.perform(
                get("/api/members/admin/" + MEMBER_ID))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").value(MEMBER_ID))
            .andExpect(jsonPath("email").value(MEMBER_EMAIL));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("관리자 단건 조회 실패 - 403 @checker 동작")
    void getOneForAdminFail() throws Exception {
        mockMvc.perform(
                get("/api/members/admin/" + MEMBER_ID))
            .andDo(print())
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("관리자 다건 조회 성공")
    void getListBySearchConditionForAdmin() throws Exception {
        mockMvc.perform(
                get("/api/members/admin"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("_embedded.memberResponseList[0].id").value(MEMBER_ID))
            .andExpect(jsonPath("_embedded.memberResponseList[0].email").value(MEMBER_EMAIL))
            .andExpect(jsonPath("_links").exists())
            .andExpect(jsonPath("page").exists());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("관리자 다건 조회 실패 - 403 @checker 동작")
    void getListBySearchConditionForAdminFail() throws Exception {
        mockMvc.perform(
                get("/api/members/admin"))
            .andDo(print())
            .andExpect(status().isForbidden());
    }
}
