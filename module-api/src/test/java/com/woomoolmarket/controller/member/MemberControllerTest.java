package com.woomoolmarket.controller.member;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woomoolmarket.ModuleApiApplication;
import com.woomoolmarket.ModuleCommonApplication;
import com.woomoolmarket.ModuleServiceApplication;
import com.woomoolmarket.common.RestDocsConfiguration;
import com.woomoolmarket.model.member.entity.Address;
import com.woomoolmarket.model.member.entity.Member;
import com.woomoolmarket.model.member.entity.MemberStatus;
import com.woomoolmarket.model.member.repository.MemberRepository;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@SpringBootTest(
    classes = {
        ModuleApiApplication.class,
        ModuleCommonApplication.class,
        ModuleServiceApplication.class
    })
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
    public void joinMember() throws Exception {

        Member member =
            Member.builder()
                .email("rj@gogo.com")
                .userId("panda")
                .nickname("horagin")
                .password("123456")
                .age("15")
                .memberStatus(MemberStatus.ACTIVE)
                .address(new Address("seoul", "yeonhui", "1234"))
                .build();

        mockMvc
            .perform(
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
                .memberStatus(MemberStatus.INACTIVE)
                .build();

        assertThat(member.getMemberStatus()).isEqualTo(MemberStatus.INACTIVE);
    }

    @Test
    public void joinMemberTest() {
        Member member =
            Member.builder()
                .email("panda@naver.com")
                .nickname("nick")
                .password("1234")
                .memberStatus(MemberStatus.INACTIVE)
                .build();
    }

    @Test
    public void mapperTest() {
        Member member =
            Member.builder()
                .email("panda@naver.com")
                .nickname("nick")
                .password("1234")
                .address(new Address("seoul", "yeonhui", "1234"))
                .memberStatus(MemberStatus.INACTIVE)
                .build();

        SignUpMemberRequest signUpMemberRequest = signUpRequestMapper.toDto(member);
        System.out.println("signUpMemberRequest = " + signUpMemberRequest);
    }

    @Test
    void getMemberTest() throws Exception {
        Member member =
            Member.builder()
                .email("panda@naver.com")
                .nickname("nick")
                .userId("ponda")
                .password("1234")
                .address(new Address("seoul", "yeonhui", "1234"))
                .memberStatus(MemberStatus.ACTIVE)
                .build();

        mockMvc.perform(
            post("/api/members")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(signUpRequestMapper.toDto(member))));

        mockMvc
            .perform(
                get("/api/members/1")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaTypes.HAL_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
            .andExpect(jsonPath("email").value("panda@naver.com"))
            .andExpect(jsonPath("address").value(new Address("seoul", "yeonhui", "1234")))
            .andExpect(jsonPath("_links.self").exists())
            .andExpect(jsonPath("_links.modify-member").exists())
            .andExpect(jsonPath("_links.leave-member").exists())
            .andDo(document("get-member"));
    }
}