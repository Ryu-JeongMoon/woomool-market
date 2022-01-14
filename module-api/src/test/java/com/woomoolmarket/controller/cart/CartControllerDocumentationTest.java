package com.woomoolmarket.controller.cart;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woomoolmarket.config.ApiDocumentationConfig;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.purchase.cart.entity.Cart;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.service.cart.dto.request.CartRequest;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser(username = "panda@naver.com", roles = "USER")
class CartControllerDocumentationTest extends ApiDocumentationConfig {

  @BeforeEach
  void init() {
    Member member = memberTestHelper.createMember();
    MEMBER_ID = member.getId();

    Product product = productTestHelper.createProduct(member);
    PRODUCT_ID = product.getId();

    Cart cart = cartTestHelper.get(member, product);
    CART_ID = cart.getId();

    Objects.requireNonNull(stringRedisTemplate.keys("*")).forEach(k -> stringRedisTemplate.delete(k));
  }

  @Test
  @DisplayName("특정 회원 장바구니 조회")
  void getListByMember() throws Exception {
    mockMvc.perform(
        get("/api/carts/" + MEMBER_ID)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaType.ALL))
      .andDo(document("cart/get-carts",
        relaxedResponseFields(
          fieldWithPath("_embedded.cartQueryResponseList[].id").type(JsonFieldType.NUMBER).description("장바구니 고유 번호"),
          fieldWithPath("_embedded.cartQueryResponseList[].quantity").type(JsonFieldType.NUMBER).description("주문 수량"),
          subsectionWithPath("_embedded.cartQueryResponseList[].memberQueryResponse").type(JsonFieldType.OBJECT)
            .description("주문 회원"),
          subsectionWithPath("_embedded.cartQueryResponseList[].productQueryResponse").type(JsonFieldType.OBJECT)
            .description("주문 상품")
        )));
  }

  @Test
  @DisplayName("장바구니 추가")
  void addToCart() throws Exception {
    CartRequest cartRequest = CartRequest.builder()
      .memberId(MEMBER_ID)
      .productId(PRODUCT_ID)
      .quantity(500).build();

    mockMvc.perform(
        post("/api/carts/" + MEMBER_ID)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaType.ALL)
          .content(objectMapper.writeValueAsString(cartRequest)))
      .andDo(document("cart/add-cart",
        requestFields(
          fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("주문 회원 고유 번호")
            .attributes(key(CONSTRAINT).value("Not Null")),
          fieldWithPath("productId").type(JsonFieldType.NUMBER).description("상품 고유 번호")
            .attributes(key(CONSTRAINT).value("Not Null")),
          fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("주문 수량")
            .attributes(key(CONSTRAINT).value("Not Null"))
        )));
  }

  @Test
  @DisplayName("특정 회원 장바구니 전체 삭제")
  void removeAll() throws Exception {
    mockMvc.perform(
        delete("/api/carts/" + MEMBER_ID)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaType.ALL))
      .andExpect(status().isNoContent())
      .andDo(document("cart/delete-carts"));
  }

  @Test
  @DisplayName("장바구니 단건 조회")
  void getOneById() throws Exception {
    mockMvc.perform(
        get("/api/carts/" + MEMBER_ID + "/" + CART_ID)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaType.ALL))
      .andDo(document("cart/get-cart",
        responseFields(
          fieldWithPath("id").type(JsonFieldType.NUMBER).description("장바구니 고유 번호"),
          fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("주문 수량"),
          subsectionWithPath("memberResponse").type(JsonFieldType.OBJECT).description("주문 회원"),
          subsectionWithPath("productResponse").type(JsonFieldType.OBJECT).description("주문 상품"),
          subsectionWithPath("_links").type(JsonFieldType.VARIES).description("HATEOAS")
        )));
  }

  @Test
  @DisplayName("장바구니 단건 삭제")
  void removeOne() throws Exception {
    mockMvc.perform(
        delete("/api/carts/" + MEMBER_ID + "/" + CART_ID)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaType.ALL))
      .andExpect(status().isNoContent())
      .andDo(document("cart/delete-cart"));
  }
}