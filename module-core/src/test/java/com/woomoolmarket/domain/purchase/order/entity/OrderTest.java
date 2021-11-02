package com.woomoolmarket.domain.purchase.order.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.common.embeddable.Delivery;
import com.woomoolmarket.domain.member.entity.Address;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.purchase.order_product.entity.OrderProduct;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.domain.purchase.product.entity.ProductCategory;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("ORDER-STATUS 기본 ONGOING")
    void createTest() {
        Member panda = Member.builder()
            .email("panda")
            .password("1234").build();

        Order order = Order.builder()
            .member(panda)
            .build();

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.ONGOING);
    }

    @Test
    @DisplayName("주문 취소 시, OrderStatus -> CANCELED")
    void cancel() {
        Product product = Product.builder()
            .price(5000)
            .stock(5000)
            .name("panda")
            .productCategory(ProductCategory.MEAT)
            .description("panda bear")
            .build();

        OrderProduct orderProduct = OrderProduct
            .builder()
            .product(product)
            .quantity(3000)
            .build();

        Order order = Order.builder()
            .orderProducts(List.of(orderProduct))
            .delivery(new Delivery("panda", new Address("1", "2", "3"), "010101010"))
            .build();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.ONGOING);

        order.cancel();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCELED);
    }
}