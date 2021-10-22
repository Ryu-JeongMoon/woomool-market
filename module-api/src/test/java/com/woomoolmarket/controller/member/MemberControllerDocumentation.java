package com.woomoolmarket.controller.member;


import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

class MemberControllerDocumentation extends ApiDocumentationConfig {

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
    @DisplayName("회원조회")
    @WithMockUser(username = USERNAME, roles = "USER")
    void findMemberTest() throws Exception {
        mockMvc.perform(
                get("/api/members/1")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaTypes.HAL_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
            .andDo(document("member/get-member",
                responseFields(
                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("고유 번호"),
                    fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                    fieldWithPath("nickname").type(JsonFieldType.STRING).description("이름 또는 별칭"),
                    fieldWithPath("profileImage").type(JsonFieldType.STRING).description("프로필 사진").optional(),
                    fieldWithPath("phone").type(JsonFieldType.STRING).description("전화 번호").optional(),
                    fieldWithPath("license").type(JsonFieldType.STRING).description("사업자 번호").optional(),
                    fieldWithPath("createdDateTime").type(JsonFieldType.VARIES).description("회원 가입일"),
                    fieldWithPath("lastModifiedDateTime").type(JsonFieldType.VARIES).description("최종 수정일"),
                    fieldWithPath("leaveDate").type(JsonFieldType.VARIES).description("탈퇴일").optional(),
                    fieldWithPath("authority").type(JsonFieldType.STRING).description("권한"),
                    fieldWithPath("authProvider").type(JsonFieldType.STRING).description("소셜 로그인 여부").optional(),
                    fieldWithPath("status").type(JsonFieldType.STRING).description("활성화 여부"),
                    subsectionWithPath("address").type(JsonFieldType.OBJECT).description("주소 - 도시명, 도로명, 우편번호").optional(),
                    subsectionWithPath("_links").type(JsonFieldType.OBJECT).description("HATEOAS")
                )));
    }

    @Test
    @DisplayName("회원가입")
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
            .andDo(document("member/join-member",
                requestFields(
                    fieldWithPath("email").type(JsonFieldType.STRING).description("회원 아이디로 사용될 이메일")
                        .attributes(key("constraint").value("이메일 형식, 9-64자")),
                    fieldWithPath("nickname").type(JsonFieldType.STRING).description("회원 이름 or 별칭")
                        .attributes(key("constraint").value("문자 형식, 4-24자")),
                    fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                        .attributes(key("constraint").value("문자 형식, 4-24자")),
                    fieldWithPath("address.city").type(JsonFieldType.STRING).description("주소 - 도시명").optional()
                        .attributes(key("constraint").value("문자 형식, 2-24자")),
                    fieldWithPath("address.street").type(JsonFieldType.STRING).description("주소 - 도로명").optional()
                        .attributes(key("constraint").value("문자 형식, 2-24자")),
                    fieldWithPath("address.zipcode").type(JsonFieldType.STRING).description("주소 - 우편번호").optional()
                        .attributes(key("constraint").value("문자 형식, 5-6자"))
                )));
    }

    @Test
    @DisplayName("회원 로그인")
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
            .andDo(document("member/login-member",
                requestFields(
                    fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
                        .attributes(key("constraint").value("이메일 형식 9-64자")),
                    fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                        .attributes(key("constraint").value("문자 형식 4-24자"))
                )));
    }

    @Test
    @DisplayName("회원 정보 수정")
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
            .andDo(document("member/modify-member",
                requestFields(
                    fieldWithPath("nickname").type(JsonFieldType.STRING).description("회원 이름 또는 별칭").optional()
                        .attributes(key("constraint").value("문자 형식 6-24자")),
                    fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호").optional()
                        .attributes(key("constraint").value("문자 형식 4-24자")),
                    fieldWithPath("profileImage").type(JsonFieldType.STRING).description("프로필 사진").optional()
                        .attributes(key("constraint").value("문자 형식 최대 255자")),
                    fieldWithPath("phone").type(JsonFieldType.STRING).description("전화번호").optional()
                        .attributes(key("constraint").value("문자 형식 10-11자")),
                    fieldWithPath("license").type(JsonFieldType.STRING).description("사업자 번호").optional()
                        .attributes(key("constraint").value("문자 형식 10자")),
                    fieldWithPath("address.city").type(JsonFieldType.STRING).description("주소 - 도시명").optional()
                        .attributes(key("constraint").value("문자 형식, 2-24자")),
                    fieldWithPath("address.street").type(JsonFieldType.STRING).description("주소 - 도로명").optional()
                        .attributes(key("constraint").value("문자 형식, 2-24자")),
                    fieldWithPath("address.zipcode").type(JsonFieldType.STRING).description("주소 - 우편번호").optional()
                        .attributes(key("constraint").value("문자 형식, 5-6자"))
                )));
    }

    @Test
    @DisplayName("회원 탈퇴")
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
    @DisplayName("탈퇴 회원 복구")
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
            .andDo(document("member/admin-get-member",
                responseFields(
                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("회원 고유 번호"),
                    fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                    fieldWithPath("nickname").type(JsonFieldType.STRING).description("회원 이름 또는 별칭"),
                    fieldWithPath("profileImage").type(JsonFieldType.STRING).description("프로필 사진").optional(),
                    fieldWithPath("phone").type(JsonFieldType.STRING).description("전화번호").optional(),
                    fieldWithPath("license").type(JsonFieldType.STRING).description("사업자 번호").optional(),
                    fieldWithPath("createdDateTime").type(JsonFieldType.VARIES).description("생성일"),
                    fieldWithPath("lastModifiedDateTime").type(JsonFieldType.VARIES).description("최종 수정일"),
                    fieldWithPath("leaveDate").type(JsonFieldType.VARIES).description("탈퇴일").optional(),
                    fieldWithPath("authority").type(JsonFieldType.STRING).description("권한"),
                    subsectionWithPath("address").type(JsonFieldType.OBJECT).description("주소").optional(),
                    fieldWithPath("authProvider").type(JsonFieldType.STRING).description("소셜 로그인 여부").optional(),
                    fieldWithPath("status").type(JsonFieldType.STRING).description("회원 활성화 여부"),
                    subsectionWithPath("_links").type(JsonFieldType.OBJECT).description("HATEOAS")
                )));
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
            .andDo(document("member/admin-get-members",
                relaxedResponseFields(
                    subsectionWithPath("_embedded.memberResponseList").type(JsonFieldType.ARRAY).description("회원 리스트"),
                    subsectionWithPath("_links").type(JsonFieldType.OBJECT).description("HATEOAS"),
                    subsectionWithPath("page").type(JsonFieldType.OBJECT).description("페이지 설정")
                )));
    }
}