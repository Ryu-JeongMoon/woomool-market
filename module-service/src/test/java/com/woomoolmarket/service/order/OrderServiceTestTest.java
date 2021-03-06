package com.woomoolmarket.service.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.woomoolmarket.config.AbstractServiceTest;
import com.woomoolmarket.domain.entity.Cart;
import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.entity.Order;
import com.woomoolmarket.domain.entity.OrderProduct;
import com.woomoolmarket.domain.entity.Product;
import com.woomoolmarket.domain.entity.enumeration.OrderStatus;
import com.woomoolmarket.domain.repository.querydto.OrderSearchCondition;
import com.woomoolmarket.domain.repository.querydto.OrderQueryResponse;
import com.woomoolmarket.service.order.dto.request.OrderRequest;
import com.woomoolmarket.service.order.dto.response.OrderResponse;
import com.woomoolmarket.service.order.mapper.OrderResponseMapper;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Slf4j
class OrderServiceTestTest extends AbstractServiceTest {

  private static List<Long> normalCartIds;
  private static List<Long> overStockCartIds;

  @Autowired
  OrderResponseMapper orderResponseMapper;

  @BeforeEach
  void init() {
    Member member = memberTestHelper.createMember();
    MEMBER_ID = member.getId();

    Product product = productTestHelper.createProduct(member);
    PRODUCT_ID = product.getId();
    int stock = product.getStock().intValue();

    Cart cart1 = cartTestHelper.get(member, product);
    Cart cart2 = cartTestHelper.get(member, product);
    normalCartIds = List.of(cart1.getId(), cart2.getId());

    Cart cartOverStock = cartTestHelper.create(member, product, stock + 1);
    overStockCartIds = List.of(cartOverStock.getId());
  }

  @AfterEach
  void clear() {
    em.createNativeQuery("ALTER TABLE ORDERS ALTER COLUMN `order_id` RESTART WITH 1").executeUpdate();
    em.createNativeQuery("ALTER TABLE MEMBER ALTER COLUMN `member_id` RESTART WITH 1").executeUpdate();
    em.createNativeQuery("ALTER TABLE PRODUCT ALTER COLUMN `product_id` RESTART WITH 1").executeUpdate();
    Objects.requireNonNull(stringRedisTemplate.keys("*")).forEach(k -> stringRedisTemplate.delete(k));
  }

  @Test
  @DisplayName("???????????? ?????? ?????? ??????")
  void orderOneTest() {
    OrderRequest orderRequest = OrderRequest.builder()
      .memberId(MEMBER_ID)
      .cartIds(List.of(normalCartIds.get(0)))
      .build();
    orderService.order(orderRequest);
    assertThat(orderRepository.findByMemberId(MEMBER_ID).size()).isEqualTo(1);
  }

  @Test
  @DisplayName("???????????? ?????? ?????? ??????")
  void orderMultipleTest() {
    OrderRequest orderRequest = OrderRequest.builder()
      .memberId(MEMBER_ID)
      .cartIds(normalCartIds)
      .build();
    orderService.order(orderRequest);

    List<OrderProduct> orderProducts = orderRepository.findByMemberId(MEMBER_ID).get(0).getOrderProducts();

    assertThat(orderProducts.size()).isEqualTo(2);
  }

  @Test
  @DisplayName("?????? ?????? ?????? ??????")
  void orderOverTheStockTest() {
    OrderRequest orderRequest = OrderRequest.builder()
      .memberId(MEMBER_ID)
      .cartIds(overStockCartIds)
      .build();
    assertThrows(IllegalArgumentException.class, () -> orderService.order(orderRequest));
  }

  @Test
  @DisplayName("?????? ?????? ?????? ??????")
  void orderNonExistProductTest() {
    OrderRequest orderRequest = OrderRequest.builder()
      .memberId(MEMBER_ID)
      .build();
    assertThrows(EntityNotFoundException.class, () -> orderService.order(orderRequest));
  }

  @Test
  @DisplayName("?????? ?????? ??????")
  void findOrdersByMemberIdTest() {
    OrderRequest orderRequest = OrderRequest.builder()
      .memberId(MEMBER_ID)
      .cartIds(normalCartIds)
      .build();
    orderService.order(orderRequest);
    assertThat(orderService.searchBy(MEMBER_ID, Pageable.ofSize(10)).getTotalElements()).isEqualTo(1);
  }

  @Test
  @DisplayName("?????? ??????")
  void getListBySearchConditionForAdmin() {
    OrderRequest orderRequest = OrderRequest.builder()
      .memberId(MEMBER_ID)
      .cartIds(normalCartIds)
      .build();
    orderService.order(orderRequest);

    OrderSearchCondition condition = OrderSearchCondition.builder()
      .orderStatus(OrderStatus.ONGOING)
      .build();

    Page<OrderQueryResponse> orderQueryResponses = orderService.searchForAdminBy(condition, Pageable.ofSize(10));
    assertThat(orderQueryResponses.getTotalElements()).isEqualTo(1);
  }

  @Test
  @DisplayName("????????? ?????? ??????")
  void transferToDto() {
    OrderRequest orderRequest = OrderRequest.builder()
      .memberId(MEMBER_ID)
      .cartIds(normalCartIds)
      .build();
    orderService.order(orderRequest);

    Page<OrderQueryResponse> orderQueryResponses = orderService.searchBy(MEMBER_ID, Pageable.ofSize(10));
    OrderQueryResponse orderQueryResponse = orderQueryResponses.getContent().get(0);
    Order order = orderRepository.findById(orderQueryResponse.getId()).get();

    OrderResponse orderResponse = orderResponseMapper.toDto(order);

    assertThat(orderResponse.getId()).isEqualTo(order.getId());
    assertThat(orderResponse.getDelivery()).isEqualTo(order.getDelivery());
    assertThat(orderResponse.getOrderStatus()).isEqualTo(order.getOrderStatus());
    assertThat(orderResponse.getOrderProducts()).isEqualTo(order.getOrderProducts());
    assertThat(orderResponse.getMemberResponse().getId()).isEqualTo(order.getMember().getId());
    assertThat(orderResponse.getMemberResponse().getEmail()).isEqualTo(order.getMember().getEmail());
    assertThat(orderResponse.getMemberResponse().getPhone()).isEqualTo(order.getMember().getPhone());
  }
}