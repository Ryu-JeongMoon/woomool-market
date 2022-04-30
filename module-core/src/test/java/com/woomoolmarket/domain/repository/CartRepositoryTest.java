package com.woomoolmarket.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.config.AbstractDataJpaTest;
import com.woomoolmarket.domain.entity.Cart;
import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.entity.Product;
import com.woomoolmarket.domain.entity.enumeration.ProductCategory;
import com.woomoolmarket.domain.entity.enumeration.Region;
import com.woomoolmarket.domain.repository.querydto.CartQueryResponse;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Slf4j
class CartRepositoryTest extends AbstractDataJpaTest {

  private Long CART_ID;
  private Member member;
  private Product product;

  @BeforeEach
  void init() {
    member = Member.builder()
      .email("pandabear")
      .nickname("nickname")
      .password("1234")
      .build();

    product = Product.builder()
      .name("panda-bear-panda")
      .description("panda")
      .stock(5000)
      .price(10000)
      .region(Region.JEJUDO)
      .productCategory(ProductCategory.MEAT)
      .build();

    Cart cart = Cart.builder()
      .member(member)
      .product(product)
      .quantity(300)
      .build();

    memberRepository.save(member);
    productRepository.save(product);
    CART_ID = cartRepository.save(cart).getId();
  }

  @Test
  @DisplayName("회원 장바구니 찾기")
  void findByMember() {
    List<Cart> carts = cartRepository.findByMember(member);
    assertThat(carts.get(0).getId()).isEqualTo(CART_ID);
  }

  @Test
  @DisplayName("장바구니 단건 삭제")
  void deleteById() {
    cartRepository.deleteById(CART_ID);

    assertThat(cartRepository.findById(CART_ID)).isEqualTo(Optional.empty());
  }

  @Test
  @DisplayName("특정 회원 장바구니 전체 삭제")
  void deleteByMember() {
    cartRepository.deleteByMember(member);

    entityManager.flush();
    entityManager.clear();

    assertThat(cartRepository.findById(CART_ID)).isEqualTo(Optional.empty());
  }

  @Test
  @DisplayName("회원 전용 본인 장바구니 조회")
  void searchBy() {
    Page<CartQueryResponse> cartQueryResponses = cartRepository.searchBy(member.getId(), Pageable.ofSize(10));
    assertThat(cartQueryResponses.getTotalElements()).isEqualTo(1);
  }

  @Test
  @DisplayName("회원 전용 장바구니 조회 - 존재하지 않는 회원 번호")
  void searchByNoResult() {
    Page<CartQueryResponse> cartQueryResponses = cartRepository.searchBy(member.getId() + 1, Pageable.ofSize(10));
    assertThat(cartQueryResponses.getTotalElements()).isEqualTo(0);
  }

  @Test
  @DisplayName("관리자 전용 장바구니 전체 조회")
  void searchForAdminBy() {
    Page<CartQueryResponse> cartQueryResponses = cartRepository.searchForAdminBy(Pageable.ofSize(10));
    assertThat(cartQueryResponses.getTotalElements()).isEqualTo(1);
  }
}