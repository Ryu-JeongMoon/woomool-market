package com.woomoolmarket.controller.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woomoolmarket.config.ApiControllerConfig;
import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.entity.Cart;
import com.woomoolmarket.domain.entity.Order;
import com.woomoolmarket.domain.entity.OrderProduct;
import com.woomoolmarket.domain.entity.Product;
import com.woomoolmarket.service.order.dto.request.OrderDeleteRequest;
import com.woomoolmarket.service.order.dto.request.OrderRequest;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser(username = "panda@naver.com", roles = "USER")
class OrderControllerTest extends ApiControllerConfig {


  @BeforeEach
  void init() {
    Member member = memberTestHelper.createMember();
    MEMBER_ID = member.getId();

    Product product = productTestHelper.createProduct(member);
    PRODUCT_ID = product.getId();

    OrderProduct orderProduct = OrderProduct.builder()
      .product(product)
      .quantity(500)
      .build();

    Order order = orderTestHelper.createOrder(member, orderProduct);
    ORDER_ID = order.getId();

    Cart cart = cartTestHelper.get(member, product);
    CART_IDS = List.of(cart.getId());

    Objects.requireNonNull(stringRedisTemplate.keys("*")).forEach(k -> stringRedisTemplate.delete(k));
  }

  @Test
  @DisplayName("주문 조회 성공")
  void getOrders() throws Exception {
    mockMvc.perform(
        get("/api/orders/" + MEMBER_ID)
          .accept(MediaType.ALL))
      .andDo(print())
      .andExpect(jsonPath("_embedded.orderQueryResponseList[0].id").value(ORDER_ID))
      .andExpect(jsonPath("_embedded.orderQueryResponseList[0].orderStatus").value("ONGOING"))
      .andExpect(jsonPath("_embedded.orderQueryResponseList[0].orderProducts").exists())
      .andExpect(jsonPath("_embedded.orderQueryResponseList[0].delivery").exists())
      .andExpect(jsonPath("_embedded.orderQueryResponseList[0].email").exists())
      .andExpect(jsonPath("_links").exists())
      .andExpect(jsonPath("page").exists());
  }

  @Test
  @DisplayName("주문 조회 실패 - 404 존재하지 않는 회원")
  void getOrdersFail() throws Exception {
    mockMvc.perform(
        get("/api/orders/" + 0)
          .accept(MediaType.ALL))
      .andDo(print())
      .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("주문 성공")
  void createOrder() throws Exception {
    OrderRequest orderRequest = OrderRequest.builder()
      .memberId(MEMBER_ID)
      .cartIds(CART_IDS)
      .build();

    mockMvc.perform(
        post("/api/orders")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(orderRequest)))
      .andDo(print())
      .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("주문 실패 - 400 @Valid 작동")
  void createOrderFail() throws Exception {
    OrderRequest orderRequest = OrderRequest.builder()
      .memberId(MEMBER_ID)
      .build();

    mockMvc.perform(
        post("/api/orders")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(orderRequest)))
      .andDo(print())
      .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("주문 취소 성공")
  void cancelOrder() throws Exception {
    OrderDeleteRequest deleteRequest = OrderDeleteRequest.builder()
      .memberId(MEMBER_ID)
      .orderId(ORDER_ID)
      .build();

    mockMvc.perform(
        delete("/api/orders/" + MEMBER_ID)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(deleteRequest)))
      .andDo(print())
      .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("주문 취소 실패 - 404 존재하지 않는 회원")
  void cancelOrderFail() throws Exception {
    OrderDeleteRequest deleteRequest = OrderDeleteRequest.builder()
      .memberId(0L)
      .orderId(ORDER_ID)
      .build();

    mockMvc.perform(
        delete("/api/orders/" + MEMBER_ID)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(deleteRequest)))
      .andDo(print())
      .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("관리자 조회 성공")
  @WithMockUser(roles = "ADMIN")
  void getListBySearchConditionForAdmin() throws Exception {
    mockMvc.perform(
        get("/api/orders/admin")
          .contentType(MediaType.ALL))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("_embedded.orderQueryResponseList[0].id").value(ORDER_ID))
      .andExpect(jsonPath("_embedded.orderQueryResponseList[0].orderStatus").value("ONGOING"))
      .andExpect(jsonPath("_embedded.orderQueryResponseList[0].orderProducts").exists())
      .andExpect(jsonPath("_embedded.orderQueryResponseList[0].delivery").exists())
      .andExpect(jsonPath("_embedded.orderQueryResponseList[0].email").exists())
      .andExpect(jsonPath("_links").exists())
      .andExpect(jsonPath("page").exists());
  }

  @Test
  @DisplayName("관리자 조회 실패 - 403 권한 없음")
  @WithMockUser(roles = "USER")
  void getListBySearchConditionForAdminFail() throws Exception {
    mockMvc.perform(
        get("/api/orders/admin")
          .contentType(MediaType.ALL))
      .andDo(print())
      .andExpect(status().isForbidden());
  }
}