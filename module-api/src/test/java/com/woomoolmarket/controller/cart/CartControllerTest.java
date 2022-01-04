package com.woomoolmarket.controller.cart;

import static com.woomoolmarket.helper.CartTestHelper.CART_QUANTITY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woomoolmarket.config.ApiControllerConfig;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.purchase.cart.entity.Cart;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.service.cart.dto.request.CartRequest;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser(username = "panda@naver.com", roles = "USER")
class CartControllerTest extends ApiControllerConfig {

  @BeforeEach
  void init() {
    Member member = memberTestHelper.createMember();
    MEMBER_ID = member.getId();

    Product product = productTestHelper.createProduct(member);
    PRODUCT_ID = product.getId();

    Cart cart = cartTestHelper.createCart(member, product);
    CART_ID = cart.getId();

    Objects.requireNonNull(stringRedisTemplate.keys("*")).forEach(k -> stringRedisTemplate.delete(k));
  }

  @Test
  @DisplayName("장바구니 단건 조회 성공")
  void getOneById() throws Exception {
    mockMvc.perform(
        get("/api/carts/" + MEMBER_ID + "/" + CART_ID))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("id").value(CART_ID))
      .andExpect(jsonPath("quantity").value(CART_QUANTITY))
      .andExpect(jsonPath("memberResponse").exists())
      .andExpect(jsonPath("productResponse").exists());
  }

  @Test
  @DisplayName("장바구니 단건 조회 실패 - 400 잘못된 요청 (GET -> POST)")
  void getOneByIdFail() throws Exception {
    mockMvc.perform(
        post("/api/carts/" + MEMBER_ID + "/" + CART_ID))
      .andDo(print())
      .andExpect(status().isMethodNotAllowed());
  }

  @Test
  @DisplayName("장바구니 다건 조회 성공")
  void getListByMember() throws Exception {
    mockMvc.perform(
        get("/api/carts/" + MEMBER_ID))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("_embedded.cartQueryResponseList[0].id").value(CART_ID))
      .andExpect(jsonPath("_embedded.cartQueryResponseList[0].quantity").value(CART_QUANTITY))
      .andExpect(jsonPath("_embedded.cartQueryResponseList[0].memberQueryResponse").exists())
      .andExpect(jsonPath("_embedded.cartQueryResponseList[0].productQueryResponse").exists());
  }

  @Test
  @DisplayName("장바구니 다건 조회 실패 - 404 존재하지 않는 회원")
  void getListByMemberFail() throws Exception {
    mockMvc.perform(
        get("/api/carts/" + 2L))
      .andDo(print())
      .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("장바구니 추가 성공")
  void addToCart() throws Exception {
    CartRequest cartRequest = CartRequest.builder()
      .memberId(MEMBER_ID)
      .productId(PRODUCT_ID)
      .quantity(CART_QUANTITY)
      .build();

    mockMvc.perform(
        post("/api/carts/" + MEMBER_ID)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(cartRequest)))
      .andDo(print())
      .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("장바구니 추가 실패 - 400 @Valid 작동")
  void addToCartFail() throws Exception {
    CartRequest cartRequest = CartRequest.builder()
      .memberId(MEMBER_ID)
      .productId(PRODUCT_ID)
      .quantity(0)
      .build();

    mockMvc.perform(
        post("/api/carts/" + MEMBER_ID)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(cartRequest)))
      .andDo(print())
      .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("장바구니 단건 삭제 성공")
  void removeOne() throws Exception {
    mockMvc.perform(
        delete("/api/carts/" + MEMBER_ID))
      .andDo(print())
      .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("장바구니 단건 삭제 실패 - 404 존재하지 않는 회원")
  void removeOneFail() throws Exception {
    mockMvc.perform(
        delete("/api/carts/" + 0))
      .andDo(print())
      .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("장바구니 비움 성공")
  void removeAll() throws Exception {
    mockMvc.perform(
        delete("/api/carts/" + MEMBER_ID + "/" + CART_ID))
      .andDo(print())
      .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("장바구니 비움 실패 - 404 존재하지 않는 장바구니")
  void removeAllFail() throws Exception {
    mockMvc.perform(
        delete("/api/carts/" + MEMBER_ID + "/" + 0))
      .andDo(print())
      .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("관리자 장바구니 다건 조회 성공")
  @WithMockUser(roles = "ADMIN")
  void getListByAdmin() throws Exception {
    mockMvc.perform(
        get("/api/carts/admin"))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("_embedded.cartQueryResponseList[0].id").value(CART_ID))
      .andExpect(jsonPath("_embedded.cartQueryResponseList[0].quantity").value(CART_QUANTITY))
      .andExpect(jsonPath("_embedded.cartQueryResponseList[0].memberQueryResponse").exists())
      .andExpect(jsonPath("_embedded.cartQueryResponseList[0].productQueryResponse").exists());
  }
}