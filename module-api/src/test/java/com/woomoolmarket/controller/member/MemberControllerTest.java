package com.woomoolmarket.controller.member;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woomoolmarket.domain.member.entity.Address;
import com.woomoolmarket.service.member.MemberService;
import com.woomoolmarket.service.member.dto.request.SignUpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    MemberService memberService;
    @MockBean
    PasswordEncoder passwordEncoder;

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
}
