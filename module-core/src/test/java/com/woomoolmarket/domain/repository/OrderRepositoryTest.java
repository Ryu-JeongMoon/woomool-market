package com.woomoolmarket.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.config.AbstractDataJpaTest;
import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.entity.Order;
import com.woomoolmarket.domain.entity.OrderProduct;
import com.woomoolmarket.domain.entity.Product;
import com.woomoolmarket.domain.entity.enumeration.OrderStatus;
import com.woomoolmarket.domain.repository.querydto.OrderQueryResponse;
import com.woomoolmarket.domain.repository.querydto.OrderSearchCondition;
import com.woomoolmarket.helper.ProductTestHelper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Slf4j
class OrderRepositoryTest extends AbstractDataJpaTest {

  private static Long MEMBER_ID;
  private static Long ORDER_ID;

  @BeforeEach
  void init() {
    Member member = Member.builder()
      .email("cleancode")
      .nickname("bear")
      .password("1234")
      .phone("01012345678")
      .build();

    Member member1 = Member.builder()
      .email("testdriven")
      .nickname("coke")
      .password("1234")
      .phone("01012345678")
      .build();

    Member member2 = Member.builder()
      .email("effectivejava")
      .nickname("blue")
      .password("1234")
      .phone("01012345678")
      .build();

    MEMBER_ID = memberRepository.save(member).getId();
    memberRepository.save(member1);
    memberRepository.save(member2);

    Product product = ProductTestHelper.createProduct(member);
    productRepository.save(product);

    OrderProduct orderProduct1 = OrderProduct.createBy(product, 5000);
    OrderProduct orderProduct2 = OrderProduct.createBy(product, 3000);

    Order order = Order.builder()
      .member(member)
      .orderProducts(List.of(orderProduct1, orderProduct2))
      .build();

    ORDER_ID = orderRepository.save(order).getId();
  }

  @Nested
  @DisplayName("주문 조회")
  class FindOrderTest {

    @Test
    @DisplayName("주문 상태 코드 조회")
    void findByConditionForAdmin_Status() {
      // given
      OrderSearchCondition condition = OrderSearchCondition.builder()
        .orderStatus(OrderStatus.ONGOING)
        .build();

      // when
      Page<OrderQueryResponse> orderQueryResponses = orderRepository.searchForAdminBy(condition, Pageable.ofSize(10));
      OrderQueryResponse order = orderQueryResponses.getContent().get(0);

      // then
      assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.ONGOING);
    }

    @Test
    @DisplayName("회원 번호 조회")
    void findByConditionForAdmin_Id() {
      // given
      OrderSearchCondition condition = OrderSearchCondition.builder()
        .memberId(MEMBER_ID)
        .build();

      // when
      Page<OrderQueryResponse> orderQueryResponses = orderRepository.searchForAdminBy(condition, Pageable.ofSize(10));

      // then
      assertThat(orderQueryResponses.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("이메일 조회")
    void findByEmail() {
      // given
      OrderSearchCondition condition = OrderSearchCondition.builder()
        .email("clea")
        .build();

      // when
      Page<OrderQueryResponse> orderQueryResponses = orderRepository.searchForAdminBy(condition, Pageable.ofSize(10));

      // then
      assertThat(orderQueryResponses.getTotalElements()).isEqualTo(1);
    }
  }
}