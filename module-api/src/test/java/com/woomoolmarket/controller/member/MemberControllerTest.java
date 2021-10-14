package com.woomoolmarket.controller.member;


import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woomoolmarket.config.ApiDocumentationConfig;
import com.woomoolmarket.domain.member.entity.Address;
import com.woomoolmarket.service.member.dto.request.LoginRequest;
import com.woomoolmarket.service.member.dto.request.ModifyRequest;
import com.woomoolmarket.service.member.dto.request.SignUpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

class MemberControllerTest extends ApiDocumentationConfig {

    private static final String USERNAME = "panda@naver.com";
    private static final String PASSWORD = "123456";
    private static Long MEMBER_ID = 1L;

    @BeforeEach
    void initialize() throws Exception {
        em.createNativeQuery("ALTER TABLE MEMBER ALTER COLUMN `member_id` RESTART WITH 1").executeUpdate();

        SignUpRequest signUpRequest = SignUpRequest.builder()
            .email(USERNAME)
            .nickname("horagin")
            .password(PASSWORD)
            .address(new Address("seoul", "yeonhui", "1234"))
            .build();

        mockMvc.perform(
            post("/api/members")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)));
    }

    @Test
    @DisplayName("회원조회 성공")
    @WithMockUser(username = USERNAME, roles = "USER")
    void findMemberTest() throws Exception {
        mockMvc.perform(
                get("/api/members/1")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaTypes.HAL_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
            .andExpect(jsonPath("email").value("panda@naver.com"))
            .andExpect(jsonPath("address").value(new Address("seoul", "yeonhui", "1234")))
            .andExpect(jsonPath("_links.self").exists())
            .andDo(document("member/get-member"));
    }

    @Test
    @DisplayName("회원가입 성공")
    public void signUpSuccessTest() throws Exception {

        SignUpRequest signUpRequest = SignUpRequest.builder()
            .email("pandabear@gogo.com")
            .nickname("horagin")
            .password("123456")
            .address(new Address("seoul", "yeonhui", "1234"))
            .build();

        mockMvc.perform(
                post("/api/members")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(signUpRequest)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
            .andExpect(jsonPath("email").value("pandabear@gogo.com"))
            .andExpect(jsonPath("address").value(new Address("seoul", "yeonhui", "1234")))
            .andExpect(jsonPath("_links.self").exists())
            .andDo(document("member/join-member"));
    }

    @Test
    @DisplayName("회원가입 실패 - @Validation 동작한다")
    void signUpFailureTest() throws Exception {
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
    @DisplayName("login 성공하면 200 내린다")
    void loginTest() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
            .email(USERNAME)
            .password(PASSWORD)
            .build();

        mockMvc.perform(
                post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaTypes.HAL_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(loginRequest)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("grantType").exists())
            .andExpect(jsonPath("accessToken").exists())
            .andExpect(jsonPath("refreshToken").exists())
            .andExpect(jsonPath("accessTokenExpiresIn").exists())
            .andDo(document("member/login-member"));
    }

    @Test
    @DisplayName("수정하면 201 내려준다")
    @WithMockUser(username = USERNAME, roles = "USER")
    void modifyTest() throws Exception {
        ModifyRequest modifyRequest = ModifyRequest.builder()
            .nickname("kcin")
            .password("654321")
            .address(new Address("부산", "갈매기", "끼룩"))
            .build();

        mockMvc.perform(
                patch("/api/members/" + MEMBER_ID)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(modifyRequest)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andDo(document("member/modify-member"));
    }

    @Test
    @DisplayName("탈퇴하면 204 내려준다")
    @WithMockUser(username = USERNAME, roles = "USER")
    void leaveTest() throws Exception {
        mockMvc.perform(
                delete("/api/members/" + MEMBER_ID)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(document("member/leave-member"));
    }

    @Test
    @DisplayName("복구하면 201 내려준다")
    @WithMockUser(username = USERNAME, roles = "ADMIN")
    void restoreTest() throws Exception {
        memberService.leaveSoftly(MEMBER_ID);

        mockMvc.perform(
                get("/api/members/deleted/" + MEMBER_ID)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isCreated())
            .andDo(print())
            .andDo(document("member/restore-member"));
    }

    @Test
    @DisplayName("어드민 전용 단건 조회")
    @WithMockUser(username = USERNAME, roles = "ADMIN")
    void adminFindMemberTest() throws Exception {
        SignUpRequest signUpRequest = SignUpRequest.builder()
            .email("panda@gmail.com")
            .nickname("nick")
            .password("123456")
            .address(new Address("seoul", "yeonhui", "1234"))
            .build();

        memberService.joinAsMember(signUpRequest);

        mockMvc.perform(
                get("/api/members/admin/2")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaTypes.HAL_JSON))
            .andDo(print())
            .andExpect(jsonPath("email").value(signUpRequest.getEmail()))
            .andExpect(jsonPath("nickname").value(signUpRequest.getNickname()))
            .andExpect(jsonPath("_links.self").exists())
            .andExpect(jsonPath("_links.previous-member").exists())
            .andExpect(jsonPath("_links.next-member").exists())
            .andDo(document("member/admin-get-member"));
    }

    @Test
    @DisplayName("어드민 전용 전체 조회")
    @WithMockUser(username = USERNAME, roles = "ADMIN")
    void adminFindMembersTest() throws Exception {
        for (int i = 0; i < 5; i++) {
            SignUpRequest signUpRequest = SignUpRequest.builder()
                .email(String.format("panda-%d@naver.com", i + 1))
                .nickname("nick" + i + 1)
                .password("123456")
                .build();
            memberService.joinAsMember(signUpRequest);
        }

        mockMvc.perform(
                get("/api/members/admin")
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("member/admin-get-members"));
    }
}