package com.woomoolmarket.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woomoolmarket.ModuleApiApplication;
import com.woomoolmarket.service.member.mapper.SignUpMemberRequestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(classes = ModuleApiApplication.class)
class RedisConfigTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired SignUpMemberRequestMapper signUpMemberRequestMapper;

}