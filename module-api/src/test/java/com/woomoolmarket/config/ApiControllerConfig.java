package com.woomoolmarket.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woomoolmarket.common.enumeration.Region;
import com.woomoolmarket.domain.board.repository.BoardRepository;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.domain.purchase.cart.repository.CartRepository;
import com.woomoolmarket.domain.purchase.order.repository.OrderRepository;
import com.woomoolmarket.domain.purchase.product.entity.ProductCategory;
import com.woomoolmarket.domain.purchase.product.repository.ProductRepository;
import com.woomoolmarket.helper.BoardTestHelper;
import com.woomoolmarket.helper.CartTestHelper;
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

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected MemberService memberService;
    @Autowired
    protected PasswordEncoder passwordEncoder;
    @Autowired
    protected OrderRepository orderRepository;
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected ProductRepository productRepository;
    @Autowired
    protected CartRepository cartRepository;
    @Autowired
    protected BoardRepository boardRepository;
    @Autowired
    protected StringRedisTemplate stringRedisTemplate;
    @Autowired
    protected MemberTestHelper memberTestHelper;
    @Autowired
    protected ProductTestHelper productTestHelper;
    @Autowired
    protected BoardTestHelper boardTestHelper;
    @Autowired
    protected OrderTestHelper orderTestHelper;
    @Autowired
    protected CartTestHelper cartTestHelper;
}
