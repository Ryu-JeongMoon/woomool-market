package com.woomoolmarket.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woomoolmarket.domain.repository.BoardRepository;
import com.woomoolmarket.domain.repository.CartRepository;
import com.woomoolmarket.domain.repository.ImageRepository;
import com.woomoolmarket.domain.repository.MemberRepository;
import com.woomoolmarket.domain.repository.OrderRepository;
import com.woomoolmarket.domain.repository.ProductRepository;
import com.woomoolmarket.helper.BoardTestHelper;
import com.woomoolmarket.helper.CartTestHelper;
import com.woomoolmarket.helper.ImageTestHelper;
import com.woomoolmarket.helper.MemberTestHelper;
import com.woomoolmarket.helper.MultipartFileTestHelper;
import com.woomoolmarket.helper.OrderTestHelper;
import com.woomoolmarket.helper.ProductTestHelper;
import com.woomoolmarket.service.auth.AuthFindService;
import com.woomoolmarket.service.auth.AuthService;
import com.woomoolmarket.service.board.BoardService;
import com.woomoolmarket.service.cart.CartService;
import com.woomoolmarket.service.image.ImageService;
import com.woomoolmarket.service.member.MemberService;
import com.woomoolmarket.service.order.OrderService;
import com.woomoolmarket.service.product.ProductService;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Disabled
@Transactional
@SpringBootTest
public abstract class AbstractServiceTest {

  protected static Long MEMBER_ID;
  protected static Long BOARD_ID;
  protected static Long IMAGE_ID;
  protected static Long CART_ID;
  protected static Long ORDER_ID;
  protected static Long PRODUCT_ID;
  protected static PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();

  @Autowired
  protected EntityManager em;
  @Autowired
  protected ObjectMapper objectMapper;
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
  protected AuthService authService;
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
  protected AuthFindService authFindService;

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
