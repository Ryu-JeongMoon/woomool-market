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
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.purchase.order.entity.Order;
import com.woomoolmarket.domain.purchase.order_product.entity.OrderProduct;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.service.order.dto.request.OrderDeleteRequest;
import com.woomoolmarket.service.order.dto.request.OrderRequest;
import java.util.Objects;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser(username = "panda@naver.com", roles = "USER")
class OrderControllerDocumentation extends ApiDocumentationConfig {

    @BeforeEach
    void init() {
        Member member = memberTestHelper.createUser();
        MEMBER_ID = member.getId();

        Product product = productTestHelper.createProduct(member);
        PRODUCT_ID = product.getId();

        OrderProduct orderProduct = OrderProduct.createOrderProduct(product, 500);

        Order order = orderTestHelper.createOrder(member, orderProduct);
        ORDER_ID = order.getId();

        Objects.requireNonNull(stringRedisTemplate.keys("*")).forEach(k -> stringRedisTemplate.delete(k));
    }

    @Test
    @DisplayName("주문내역 조회")
    void getOrders() throws Exception {
        mockMvc.perform(
                get("/api/orders/" + MEMBER_ID)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.ALL))
            .andDo(document("order/get-orders",
                relaxedResponseFields(
                    fieldWithPath("_embedded.orderQueryResponseList[].id").type(JsonFieldType.NUMBER).description("주문 번호"),
                    fieldWithPath("_embedded.orderQueryResponseList[].orderStatus").type(JsonFieldType.STRING).description("주문 상태"),
                    subsectionWithPath("_embedded.orderQueryResponseList[].orderProducts").type(JsonFieldType.ARRAY)
                        .description("주문 상품 목록"),
                    fieldWithPath("_embedded.orderQueryResponseList[].delivery").type(JsonFieldType.OBJECT).description("배송 정보"),
                    subsectionWithPath("_embedded.orderQueryResponseList[].email").type(JsonFieldType.STRING)
                        .description("주문 회원"),
                    subsectionWithPath("_links").type(JsonFieldType.OBJECT).description("HATEOAS"),
                    subsectionWithPath("page").type(JsonFieldType.OBJECT).description("페이지 설정")
                ))
            );
    }

    @Test
    @DisplayName("주문 추가")
    void createOrder() throws Exception {
        OrderRequest orderRequest = OrderRequest.builder()
            .memberId(MEMBER_ID)
            .productId(PRODUCT_ID)
            .quantity(30)
            .build();

        mockMvc.perform(
                post("/api/orders")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.ALL)
                    .content(objectMapper.writeValueAsString(orderRequest)))
            .andDo(document("order/create-order",
                requestFields(
                    fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 고유 번호"),
                    fieldWithPath("productId").type(JsonFieldType.NUMBER).description("상품 고유 번호").optional(),
                    fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("주문 수량").optional()
                )
            ));
    }

    @Test
    @DisplayName("주문 취소")
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
                    fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 고유 번호"),
                    fieldWithPath("orderId").type(JsonFieldType.NUMBER).description("주문 번호")
                )));
    }

    @Test
    @DisplayName("관리자 주문내역 조회")
    @WithMockUser(roles = "ADMIN")
    void getListBySearchConditionForAdmin() throws Exception {
        mockMvc.perform(
                get("/api/orders/admin")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.ALL))
            .andDo(document("order/admin-get-orders",
                relaxedResponseFields(
                    fieldWithPath("_embedded.orderQueryResponseList[].id").type(JsonFieldType.NUMBER).description("주문 번호"),
                    fieldWithPath("_embedded.orderQueryResponseList[].orderStatus").type(JsonFieldType.STRING).description("주문 상태"),
                    subsectionWithPath("_embedded.orderQueryResponseList[].orderProducts").type(JsonFieldType.ARRAY)
                        .description("주문 상품 목록"),
                    fieldWithPath("_embedded.orderQueryResponseList[].delivery").type(JsonFieldType.OBJECT).description("배송 정보"),
                    subsectionWithPath("_embedded.orderQueryResponseList[].email").type(JsonFieldType.STRING)
                        .description("주문 회원"),
                    subsectionWithPath("_links").type(JsonFieldType.OBJECT).description("HATEOAS"),
                    subsectionWithPath("page").type(JsonFieldType.OBJECT).description("페이지 설정")
                )));
    }
}