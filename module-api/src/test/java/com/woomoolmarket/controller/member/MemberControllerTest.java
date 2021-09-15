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
import com.woomoolmarket.ModuleApiApplication;
import com.woomoolmarket.ModuleCoreApplication;
import com.woomoolmarket.ModuleServiceApplication;
import com.woomoolmarket.common.RestDocsConfiguration;
import com.woomoolmarket.domain.member.entity.Address;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.entity.MemberStatus;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.service.member.dto.request.LoginRequest;
import com.woomoolmarket.service.member.dto.request.SignUpMemberRequest;
import com.woomoolmarket.service.member.mapper.SignUpMemberRequestMapper;
import org.assertj.core.api.Assertions;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
        this.mockMvc =
            MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("Normal Test")
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
    public void mapperTest() {
        Member member =
            Member.builder()
                .email("panda@naver.com")
                .nickname("nick")
                .password("1234")
                .address(new Address("seoul", "yeonhui", "1234"))
                .build();

        SignUpMemberRequest signUpMemberRequest = signUpRequestMapper.toDto(member);

        Assertions.assertThat(member.getEmail()).isEqualTo(signUpMemberRequest.getEmail());
        Assertions.assertThat(member.getNickname()).isEqualTo(signUpMemberRequest.getNickname());
        Assertions.assertThat(member.getPassword()).isEqualTo(signUpMemberRequest.getPassword());
        Assertions.assertThat(member.getAddress()).isEqualTo(signUpMemberRequest.getAddress());
    }

    @Test
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
            .andExpect(status().isBadRequest());

//        mockMvc
//            .perform(
//                post("/api/members")
//                    .contentType(MediaType.APPLICATION_JSON_VALUE)
//                    .accept(MediaTypes.HAL_JSON)
//                    .header("Authorization", accessToken))
//            .andDo(print())
//            .andExpect(status().isUnauthorized());
//            .andExpect(header().exists(HttpHeaders.LOCATION))
//            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
//            .andExpect(jsonPath("email").value("panda@naver.com"))
//            .andExpect(jsonPath("address").value(new Address("seoul", "yeonhui", "1234")))
//            .andExpect(jsonPath("_links.self").exists())
//            .andExpect(jsonPath("_links.modify-member").exists())
//            .andExpect(jsonPath("_links.leave-member").exists())
//            .andDo(document("get-member"));
    }

    @Test
    void loginTest() throws Exception {

        Member member = Member.builder()
            .email("panda@naver.com")
            .nickname("nick")
            .userId("ponda")
            .password("123456")
            .address(new Address("seoul", "yeonhui", "1234"))
            .build();

        LoginRequest loginRequest = new LoginRequest(member.getEmail(), member.getPassword());

        mockMvc.perform(
            post("/api/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        ).andExpect(status().isOk());
    }
}