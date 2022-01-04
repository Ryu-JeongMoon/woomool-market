package com.woomoolmarket.config;

import com.woomoolmarket.domain.board.repository.BoardRepository;
import com.woomoolmarket.domain.image.repository.ImageRepository;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.domain.purchase.cart.repository.CartRepository;
import com.woomoolmarket.domain.purchase.order.repository.OrderRepository;
import com.woomoolmarket.domain.purchase.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(TestConfig.class)
public class DataJpaTestConfig {

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
}
