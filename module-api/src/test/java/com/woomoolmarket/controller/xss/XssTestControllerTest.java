package com.woomoolmarket.controller.xss;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.woomoolmarket.ModuleApiApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = ModuleApiApplication.class)
class XssTestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("escape 처리된 문자가 반환된다")
    void xssFilterTest() throws Exception {
        mockMvc.perform(
                get("/xss")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("htmlTdTag").value("&lt;td&gt;&lt;/td&gt;"))
            .andExpect(jsonPath("htmlTableTag").value("&lt;table&gt;"));
    }
}