package com.woomoolmarket.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woomoolmarket.domain.board.repository.BoardRepository;
import com.woomoolmarket.domain.image.repository.ImageRepository;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.domain.purchase.cart.repository.CartRepository;
import com.woomoolmarket.domain.purchase.order.repository.OrderRepository;
import com.woomoolmarket.domain.purchase.product.repository.ProductRepository;
import com.woomoolmarket.helper.BoardTestHelper;
import com.woomoolmarket.helper.CartTestHelper;
import com.woomoolmarket.helper.ImageTestHelper;
import com.woomoolmarket.helper.MemberTestHelper;
import com.woomoolmarket.helper.OrderTestHelper;
import com.woomoolmarket.helper.ProductTestHelper;
import com.woomoolmarket.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public abstract class ApiControllerConfig {

    protected static Long MEMBER_ID;
    protected static Long PRODUCT_ID;
    protected static Long CART_ID;
    protected static Long ORDER_ID;
    protected static Long BOARD_ID;
    protected static Long IMAGE_ID;

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected PasswordEncoder passwordEncoder;
    @Autowired
    protected StringRedisTemplate stringRedisTemplate;

    @Autowired
    protected CartRepository cartRepository;
    @Autowired
    protected BoardRepository boardRepository;
    @Autowired
    protected ImageRepository imageRepository;
    @Autowired
    protected OrderRepository orderRepository;
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected MemberService memberService;

    @Autowired
    protected CartTestHelper cartTestHelper;
    @Autowired
    protected OrderTestHelper orderTestHelper;
    @Autowired
    protected BoardTestHelper boardTestHelper;
    @Autowired
    protected ImageTestHelper imageTestHelper;
    @Autowired
    protected MemberTestHelper memberTestHelper;
    @Autowired
    protected ProductTestHelper productTestHelper;

}
