package com.woomoolmarket.service.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.woomoolmarket.config.ServiceTestConfig;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.purchase.order.entity.OrderStatus;
import com.woomoolmarket.domain.purchase.order.repository.OrderSearchCondition;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.service.order.dto.request.OrderRequest;
import com.woomoolmarket.service.order.dto.response.OrderResponse;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Log4j2
class OrderServiceTestTest extends ServiceTestConfig {

    private static int PRODUCT_STOCK;

    @BeforeEach
    void init() {
        Member member = memberTestHelper.createUser();
        MEMBER_ID = member.getId();

        Product product = productTestHelper.createProduct(member);
        PRODUCT_ID = product.getId();
        PRODUCT_STOCK = product.getStock();

        cartTestHelper.createCart(member, product);
    }

    @AfterEach
    void clear() {
        em.createNativeQuery("ALTER TABLE ORDERS ALTER COLUMN `order_id` RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE MEMBER ALTER COLUMN `member_id` RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE PRODUCT ALTER COLUMN `product_id` RESTART WITH 1").executeUpdate();
        Objects.requireNonNull(stringRedisTemplate.keys("*")).forEach(k -> stringRedisTemplate.delete(k));
    }

    @Test
    @DisplayName("단건 주문")
    void orderOneTest() {
        OrderRequest orderRequest = OrderRequest.builder()
            .memberId(MEMBER_ID)
            .productId(PRODUCT_ID)
            .quantity(3).build();
        orderService.orderOne(orderRequest);
        assertThat(orderRepository.findById(1L)).isNotNull();
    }

    @Test
    @DisplayName("다건 주문")
    void orderMultipleTest() {
        OrderRequest orderRequest = OrderRequest.builder()
            .memberId(MEMBER_ID)
            .build();
        orderService.orderMultiples(orderRequest);
        assertThat(orderRepository.findById(1L)).isNotNull();
    }

    @Test
    @DisplayName("재고 이상 주문 불가")
    void orderOverTheStockTest() {
        OrderRequest orderRequest = OrderRequest.builder()
            .memberId(MEMBER_ID)
            .productId(PRODUCT_ID)
            .quantity(PRODUCT_STOCK + 1)
            .build();
        assertThrows(IllegalArgumentException.class, () -> orderService.orderOne(orderRequest));
    }

    @Test
    @DisplayName("없는 상품 주문 불가")
    void orderNonExistProductTest() {
        OrderRequest orderRequest = OrderRequest.builder()
            .memberId(MEMBER_ID)
            .productId(5L)
            .quantity(3).build();
        assertThrows(EntityNotFoundException.class, () -> orderService.orderOne(orderRequest));
    }

    @Test
    @DisplayName("주문 내역 조회")
    void findOrdersByMemberIdTest() {
        assertThat(orderService.getListByMemberId(MEMBER_ID)).isNotNull();
    }

    @Test
    @DisplayName("주문 검색")
    void getListBySearchConditionForAdmin() {
        OrderRequest orderRequest = OrderRequest.builder()
            .memberId(MEMBER_ID)
            .productId(PRODUCT_ID)
            .quantity(3).build();
        orderService.orderOne(orderRequest);

        OrderSearchCondition condition = OrderSearchCondition.builder()
            .orderStatus(OrderStatus.ONGOING)
            .build();

        List<OrderResponse> orderResponses = orderService.getListBySearchCondition(condition);
        assertThat(orderResponses.size()).isEqualTo(1);
    }
}