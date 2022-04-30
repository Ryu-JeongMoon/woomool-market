package com.woomoolmarket.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woomoolmarket.domain.repository.BoardRepository;
import com.woomoolmarket.domain.repository.CartRepository;
import com.woomoolmarket.domain.repository.ImageRepository;
import com.woomoolmarket.domain.repository.MemberRepository;
import com.woomoolmarket.domain.repository.OrderRepository;
import com.woomoolmarket.domain.repository.ProductRepository;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(TestConfig.class)
public class AbstractDataJpaTest {

  @Autowired
  protected EntityManager entityManager;
  @Autowired
  protected JPAQueryFactory queryFactory;
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
