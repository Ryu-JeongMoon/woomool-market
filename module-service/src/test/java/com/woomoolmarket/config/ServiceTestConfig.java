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
import com.woomoolmarket.helper.MultipartFileTestHelper;
import com.woomoolmarket.helper.OrderTestHelper;
import com.woomoolmarket.helper.ProductTestHelper;
import com.woomoolmarket.service.board.BoardService;
import com.woomoolmarket.service.cart.CartService;
import com.woomoolmarket.service.image.ImageService;
import com.woomoolmarket.service.member.MemberService;
import com.woomoolmarket.service.order.OrderService;
import com.woomoolmarket.service.product.ProductService;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public abstract class ServiceTestConfig {

    protected static Long MEMBER_ID;
    protected static Long BOARD_ID;
    protected static Long IMAGE_ID;
    protected static Long CART_ID;
    protected static Long ORDER_ID;
    protected static Long PRODUCT_ID;

    @Autowired
    protected EntityManager em;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected PasswordEncoder passwordEncoder;
    @Autowired
    protected StringRedisTemplate stringRedisTemplate;

    @Autowired
    protected CartRepository cartRepository;
    @Autowired
    protected OrderRepository orderRepository;
    @Autowired
    protected BoardRepository boardRepository;
    @Autowired
    protected ImageRepository imageRepository;
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected CartService cartService;
    @Autowired
    protected OrderService orderService;
    @Autowired
    protected BoardService boardService;
    @Autowired
    protected ImageService imageService;
    @Autowired
    protected MemberService memberService;
    @Autowired
    protected ProductService productService;

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
    @Autowired
    protected MultipartFileTestHelper multipartFileTestHelper;

}
