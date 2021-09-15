package com.woomoolmarket.controller.board;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.woomoolmarket.ModuleApiApplication;
import com.woomoolmarket.ModuleCoreApplication;
import com.woomoolmarket.ModuleServiceApplication;
import com.woomoolmarket.common.RestDocsConfiguration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@SpringBootTest(classes = {ModuleApiApplication.class, ModuleServiceApplication.class, ModuleCoreApplication.class})
class BoardControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    WebApplicationContext context;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void localDateTimeMapperTest() throws JsonProcessingException {
        LocalDateTime now = LocalDateTime.now();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // 추가
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String result = mapper.writeValueAsString(now);
        System.out.println("result = " + result);

        Assertions.assertThat(now.toString()).isEqualTo(result);
    }
}