package com.woomoolmarket.service.cart;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.config.AbstractServiceTest;
import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.entity.Cart;
import com.woomoolmarket.domain.repository.querydto.CartQueryResponse;
import com.woomoolmarket.domain.entity.Product;
import com.woomoolmarket.service.cart.dto.request.CartRequest;
import com.woomoolmarket.service.cart.dto.response.CartResponse;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Slf4j
class CartServiceTest extends AbstractServiceTest {

  @BeforeEach
  void init() {
    Member member = memberTestHelper.createMember();
    MEMBER_ID = memberRepository.save(member).getId();

    Product product = productTestHelper.createProduct(member);
    PRODUCT_ID = productRepository.save(product).getId();

    Cart cart = cartTestHelper.get(member, product);
    CART_ID = cartRepository.save(cart).getId();
  }

  @AfterEach
  void clear() {
    Objects.requireNonNull(stringRedisTemplate.keys("*")).forEach(k -> stringRedisTemplate.delete(k));
  }

  @Test
  @DisplayName("장바구니 단건 조회")
  void getById() {
    CartResponse cartResponse = cartService.findBy(CART_ID);

    assertThat(cartResponse.getMemberResponse().getEmail()).isEqualTo("panda@naver.com");
    assertThat(cartResponse.getProductResponse().getPrice()).isEqualTo(50000);
    assertThat(cartResponse.getQuantity()).isEqualTo(500);
  }

  @Test
  @DisplayName("장바구니 다건 조회")
  void getListByMember() {
    Page<CartQueryResponse> cartQueryResponses = cartService.searchBy(MEMBER_ID, Pageable.ofSize(10));
    assertThat(cartQueryResponses.getTotalElements()).isEqualTo(1);
  }

  @Test
  @DisplayName("장바구니 추가")
  void add() {
    CartRequest cartRequest = CartRequest.builder()
      .memberId(MEMBER_ID)
      .productId(PRODUCT_ID)
      .quantity(500)
      .build();

    Long cartId = cartService.add(cartRequest);
    assertThat(cartId).isNotNull();
  }

  @Test
  @DisplayName("장바구니 단건 삭제")
  void remove() {
    cartService.remove(CART_ID);
    Page<CartQueryResponse> cartQueryResponses = cartService.searchBy(MEMBER_ID, Pageable.ofSize(10));
    assertThat(cartQueryResponses.getTotalElements()).isEqualTo(0);
  }

  @Test
  @DisplayName("장바구니 다건 삭제")
  void removeAll() {
    cartService.removeAll(MEMBER_ID);
    Page<CartQueryResponse> cartQueryResponses = cartService.searchBy(MEMBER_ID, Pageable.ofSize(10));
    assertThat(cartQueryResponses.getTotalElements()).isEqualTo(0);
  }
}