package com.woomoolmarket.controller.member;


import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woomoolmarket.common.RestDocsConfiguration;
import com.woomoolmarket.domain.member.entity.Address;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.service.member.MemberService;
import com.woomoolmarket.service.member.dto.request.LoginRequest;
import com.woomoolmarket.service.member.dto.request.ModifyRequest;
import com.woomoolmarket.service.member.dto.request.SignUpRequest;
import com.woomoolmarket.service.member.dto.response.MemberResponse;
import com.woomoolmarket.service.member.mapper.SignUpRequestMapper;
import javax.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@Log4j2
@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@SpringBootTest
class MemberControllerTest implements BeforeTestExecutionCallback {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    WebApplicationContext context;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    SignUpRequestMapper signUpRequestMapper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    EntityManager em;
    @Autowired
    MemberService memberService;

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
            .apply(documentationConfiguration(restDocumentation))
            .build();
    }

    private static final String USERNAME = "panda@naver.com";
    private static final String PASSWORD = "123456";

    @BeforeEach
    void initialize() {
        memberRepository.deleteAll();
        em.createNativeQuery("ALTER TABLE MEMBER ALTER COLUMN `member_id` RESTART WITH 1").executeUpdate();

        SignUpRequest signUpRequest = SignUpRequest.builder()
            .email(USERNAME)
            .nickname("horagin")
            .password(PASSWORD)
            .address(new Address("seoul", "yeonhui", "1234"))
            .build();

        Long findResult = memberService.joinAsMember(signUpRequest).getId();
    }

    @Test
    @DisplayName("회원조회 성공")
    @WithMockUser(username = "panda@naver.com", roles = "USER")
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
            .andDo(document("get-member"));
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
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
            .andExpect(jsonPath("email").value("pandabear@gogo.com"))
            .andExpect(jsonPath("address").value(new Address("seoul", "yeonhui", "1234")))
            .andExpect(jsonPath("_links.self").exists())
            .andDo(document("join-member"));
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

    // 이거 왜 깨지는거지..
    // -> Security Config 에서 /api/login 경로 허용 안 해줬기 때문!
    @Test
    @DisplayName("login 성공하면 200 내린다")
    void loginTest() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
            .email(USERNAME)
            .password(PASSWORD)
            .build();

        mockMvc.perform(
                post("/api/login")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaTypes.HAL_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(loginRequest)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("grantType").exists())
            .andExpect(jsonPath("accessToken").exists())
            .andExpect(jsonPath("refreshToken").exists())
            .andExpect(jsonPath("accessTokenExpiresIn").exists())
            .andDo(document("login-member"));
    }

    @Test
    @DisplayName("수정하면 201 내려준다")
    @WithMockUser(username = "panda@naver.com", roles = "USER")
    void modifyTest() throws Exception {
        ModifyRequest modifyRequest = ModifyRequest.builder()
            .nickname("kcin")
            .password("654321")
            .address(new Address("부산", "갈매기", "끼룩"))
            .build();

        mockMvc.perform(
                patch("/api/members/1")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(modifyRequest)))
            .andDo(print())
            .andExpect(jsonPath("email").exists())
            .andExpect(jsonPath("nickname").value("kcin"))
            .andExpect(jsonPath("address").exists())
            .andDo(document("modify-member"));
    }

    @Test
    @DisplayName("탈퇴하면 204 내려준다")
    @WithMockUser(username = "panda@naver.com", roles = "USER")
    void leaveTest() throws Exception {
        mockMvc.perform(
                delete("/api/members/1")
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(document("leave-member"));
    }

    @Test
    @DisplayName("어드민 전용 단건 조회")
    @WithMockUser(username = "panda@gmail.com", roles = "ADMIN")
    void adminFindMemberTest() throws Exception {
        SignUpRequest signUpRequest = SignUpRequest.builder()
            .email("panda@gmail.com")
            .nickname("nick")
            .password("123456")
            .address(new Address("seoul", "yeonhui", "1234"))
            .build();

        Long findResult = memberService.joinAsMember(signUpRequest).getId();

        mockMvc.perform(
                get("/api/members/admin-only/" + findResult)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaTypes.HAL_JSON))
            .andDo(print())
            .andExpect(jsonPath("email").value(signUpRequest.getEmail()))
            .andExpect(jsonPath("nickname").value(signUpRequest.getNickname()))
            .andExpect(jsonPath("_links.self").exists())
            .andExpect(jsonPath("_links.previous-member").exists())
            .andExpect(jsonPath("_links.next-member").exists())
            .andDo(document("admin-only-get-member"));
    }

    @Test
    @DisplayName("어드민 전용 전체 조회")
    @WithMockUser(username = "panda@gmail.com", roles = "ADMIN")
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
                get("/api/members/admin-only/all")
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("admin-only-get-members"));
    }
}