package com.woomoolmarket.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woomoolmarket.common.RestDocsConfiguration;
import com.woomoolmarket.domain.board.repository.BoardRepository;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.domain.purchase.cart.repository.CartRepository;
import com.woomoolmarket.domain.purchase.order.repository.OrderRepository;
import com.woomoolmarket.domain.purchase.product.repository.ProductRepository;
import com.woomoolmarket.service.board.BoardService;
import com.woomoolmarket.service.board.mapper.BoardResponseMapper;
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
import org.springframework.web.context.WebApplicationContext;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriPort = 8443)
@Import(RestDocsConfiguration.class)
public class ApiDocumentationConfig {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected WebApplicationContext context;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected EntityManager em;
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected MemberService memberService;
    @Autowired
    protected BoardRepository boardRepository;
    @Autowired
    protected BoardService boardService;
    @Autowired
    protected OrderRepository orderRepository;
    @Autowired
    protected OrderService orderService;
    @Autowired
    protected CartRepository cartRepository;
    @Autowired
    protected CartService cartService;
    @Autowired
    protected ProductRepository productRepository;
    @Autowired
    protected ProductService productService;

}
