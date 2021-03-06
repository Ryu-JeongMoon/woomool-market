package com.woomoolmarket.controller.order;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.woomoolmarket.config.ApiDocumentationConfig;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser(username = "panda@naver.com", roles = "USER")
class OrderControllerDocumentationTest extends ApiDocumentationConfig {

  @BeforeEach
  void init() {
    Member member = memberTestHelper.createMember();
    MEMBER_ID = member.getId();

    Product product = productTestHelper.createProduct(member);
    PRODUCT_ID = product.getId();

    OrderProduct orderProduct = OrderProduct.createBy(product, 500);

    Order order = orderTestHelper.createOrder(member, orderProduct);
    ORDER_ID = order.getId();

    Cart cart = cartTestHelper.get(member, product);
    CART_IDS = List.of(cart.getId());

    Objects.requireNonNull(stringRedisTemplate.keys("*")).forEach(k -> stringRedisTemplate.delete(k));
  }

  @Test
  @DisplayName("???????????? ??????")
  void getOrders() throws Exception {
    mockMvc.perform(
        get("/api/orders/" + MEMBER_ID)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaType.ALL))
      .andDo(document("order/get-orders",
        relaxedResponseFields(
          fieldWithPath("_embedded.orderQueryResponseList[].id").type(JsonFieldType.NUMBER).description("?????? ??????"),
          fieldWithPath("_embedded.orderQueryResponseList[].orderStatus").type(JsonFieldType.STRING).description("?????? ??????"),
          subsectionWithPath("_embedded.orderQueryResponseList[].orderProducts").type(JsonFieldType.ARRAY)
            .description("?????? ?????? ??????"),
          fieldWithPath("_embedded.orderQueryResponseList[].delivery").type(JsonFieldType.OBJECT).description("?????? ??????"),
          subsectionWithPath("_embedded.orderQueryResponseList[].email").type(JsonFieldType.STRING)
            .description("?????? ??????"),
          subsectionWithPath("_links").type(JsonFieldType.OBJECT).description("HATEOAS"),
          subsectionWithPath("page").type(JsonFieldType.OBJECT).description("????????? ??????")
        ))
      );
  }

  @Test
  @DisplayName("?????? ??????")
  void createOrder() throws Exception {
    OrderRequest orderRequest = OrderRequest.builder()
      .memberId(MEMBER_ID)
      .cartIds(CART_IDS)
      .build();

    mockMvc.perform(
        post("/api/orders")
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaType.ALL)
          .content(objectMapper.writeValueAsString(orderRequest)))
      .andDo(document("order/create-order",
        requestFields(
          fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("?????? ?????? ??????"),
          fieldWithPath("cartIds").type(JsonFieldType.ARRAY).description("???????????? ?????? ??????")
        )
      ));
  }

  @Test
  @DisplayName("?????? ??????")
  void cancelOrder() throws Exception {
    OrderDeleteRequest deleteRequest = OrderDeleteRequest.builder()
      .memberId(MEMBER_ID)
      .orderId(ORDER_ID)
      .build();

    mockMvc.perform(
        delete("/api/orders/" + MEMBER_ID)
          .content(objectMapper.writeValueAsString(deleteRequest))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.ALL))
      .andDo(document("order/delete-order",
        requestFields(
          fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("?????? ?????? ??????"),
          fieldWithPath("orderId").type(JsonFieldType.NUMBER).description("?????? ??????")
        )));
  }

  @Test
  @DisplayName("????????? ???????????? ??????")
  @WithMockUser(roles = "ADMIN")
  void getListBySearchConditionForAdmin() throws Exception {
    mockMvc.perform(
        get("/api/orders/admin")
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaType.ALL))
      .andDo(document("order/admin-get-orders",
        relaxedResponseFields(
          fieldWithPath("_embedded.orderQueryResponseList[].id").type(JsonFieldType.NUMBER).description("?????? ??????"),
          fieldWithPath("_embedded.orderQueryResponseList[].orderStatus").type(JsonFieldType.STRING).description("?????? ??????"),
          subsectionWithPath("_embedded.orderQueryResponseList[].orderProducts").type(JsonFieldType.ARRAY)
            .description("?????? ?????? ??????"),
          fieldWithPath("_embedded.orderQueryResponseList[].delivery").type(JsonFieldType.OBJECT).description("?????? ??????"),
          subsectionWithPath("_embedded.orderQueryResponseList[].email").type(JsonFieldType.STRING)
            .description("?????? ??????"),
          subsectionWithPath("_links").type(JsonFieldType.OBJECT).description("HATEOAS"),
          subsectionWithPath("page").type(JsonFieldType.OBJECT).description("????????? ??????")
        )));
  }
}