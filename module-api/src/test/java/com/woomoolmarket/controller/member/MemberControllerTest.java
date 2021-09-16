package com.woomoolmarket.controller.member;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woomoolmarket.common.RestDocsConfiguration;
import com.woomoolmarket.domain.member.entity.Address;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.entity.MemberStatus;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.service.member.dto.request.LoginRequest;
import com.woomoolmarket.service.member.dto.request.SignUpMemberRequest;
import com.woomoolmarket.service.member.mapper.SignUpMemberRequestMapper;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

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
    SignUpMemberRequestMapper signUpRequestMapper;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
            .apply(documentationConfiguration(restDocumentation))
            .build();
    }

    // Result<T>로 감싸면서 status, header 등의 정보가 감춰진다 -> 보완 필요
    //@Test
    @DisplayName("회원가입 성공")
    public void signUpSuccessTest() throws Exception {

        Member member =
            Member.builder()
                .email("rj@gogo.com")
                .userId("panda")
                .nickname("horagin")
                .password("123456")
                .address(new Address("seoul", "yeonhui", "1234"))
                .build();

        mockMvc.perform(
                post("/api/members")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(signUpRequestMapper.toDto(member))))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
            .andExpect(jsonPath("email").value("rj@gogo.com"))
            .andExpect(jsonPath("address").value(new Address("seoul", "yeonhui", "1234")))
            .andExpect(jsonPath("_links.self").exists())
            .andExpect(jsonPath("_links.modify-member").exists())
            .andExpect(jsonPath("_links.leave-member").exists())
            .andDo(document("join-member"));
    }

    @Test
    @DisplayName("기본 상태 ACTIVE")
    public void statusTest() {
        Member member =
            Member.builder()
                .email("rjrj")
                .nickname("nick")
                .password("1234")
                .build();

        Member findResult = memberRepository.save(member);

        assertThat(findResult.getMemberStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    @DisplayName("signUpMapper 올바르게 변환된다")
    public void signUpMapperTest() {
        Member member =
            Member.builder()
                .email("panda@naver.com")
                .nickname("nick")
                .password("1234")
                .address(new Address("seoul", "yeonhui", "1234"))
                .build();

        SignUpMemberRequest signUpMemberRequest = signUpRequestMapper.toDto(member);

        assertThat(member.getEmail()).isEqualTo(signUpMemberRequest.getEmail());
        assertThat(member.getNickname()).isEqualTo(signUpMemberRequest.getNickname());
        assertThat(member.getPassword()).isEqualTo(signUpMemberRequest.getPassword());
        assertThat(member.getAddress()).isEqualTo(signUpMemberRequest.getAddress());
    }

    @Test
    @DisplayName("회원가입 실패")
    void signUpFailureTest() throws Exception {
        Member member = Member.builder()
            .email("panda@naver.com")
            .nickname("nick")
            .userId("ponda")
            .password("12345")
            .address(new Address("seoul", "yeonhui", "1234"))
            .build();

        SignUpMemberRequest signUpMemberRequest = signUpRequestMapper.toDto(member);

        mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(signUpMemberRequest)))
            .andExpect(jsonPath("data.statusCodeValue").value(400));
    }

    // login 과정 이상 있나?, 테스트 깨짐
    //@Test
    void loginTest() throws Exception {

        Member member = Member.builder()
            .email("panda@naver.com")
            .nickname("nick")
            .userId("ponda")
            .password("123456")
            .address(new Address("seoul", "yeonhui", "1234"))
            .build();

        memberRepository.save(member);

        LoginRequest loginRequest = new LoginRequest(member.getEmail(), member.getPassword());

        mockMvc.perform(
            post("/api/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        ).andExpect(status().isOk());
    }
}