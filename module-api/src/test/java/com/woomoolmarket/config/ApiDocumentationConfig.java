package com.woomoolmarket.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woomoolmarket.common.RestDocsConfiguration;
import com.woomoolmarket.service.board.BoardService;
import com.woomoolmarket.service.cart.CartService;
import com.woomoolmarket.service.member.MemberService;
import com.woomoolmarket.service.order.OrderService;
import com.woomoolmarket.service.product.ProductService;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
public class ApiDocumentationConfig {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected EntityManager em;
    @Autowired
    protected MemberService memberService;
    @Autowired
    protected BoardService boardService;
    @Autowired
    protected OrderService orderService;
    @Autowired
    protected CartService cartService;
    @Autowired
    protected ProductService productService;

}
