package com.woomoolmarket.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woomoolmarket.ModuleApiApplication;
import com.woomoolmarket.domain.repository.BoardRepository;
import com.woomoolmarket.domain.repository.ImageRepository;
import com.woomoolmarket.domain.repository.MemberRepository;
import com.woomoolmarket.domain.repository.CartRepository;
import com.woomoolmarket.domain.repository.OrderRepository;
import com.woomoolmarket.domain.repository.ProductRepository;
import com.woomoolmarket.helper.BoardTestHelper;
import com.woomoolmarket.helper.CartTestHelper;
import com.woomoolmarket.helper.ImageTestHelper;
import com.woomoolmarket.helper.MemberTestHelper;
import com.woomoolmarket.helper.MultipartFileTestHelper;
import com.woomoolmarket.helper.OrderTestHelper;
import com.woomoolmarket.helper.ProductTestHelper;
import com.woomoolmarket.service.member.MemberService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(classes = ModuleApiApplication.class)
@AutoConfigureMockMvc(addFilters = false)
public abstract class ApiControllerConfig {

  protected static Long MEMBER_ID;
  protected static Long PRODUCT_ID;
  protected static Long CART_ID;
  protected static Long ORDER_ID;
  protected static Long BOARD_ID;
  protected static Long IMAGE_ID;
  protected static List<Long> CART_IDS;
  protected static List<Long> CART_IDS_TO_BE_FAILED;

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
  @Autowired
  protected MultipartFileTestHelper multipartFileTestHelper;

}
