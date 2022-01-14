package com.woomoolmarket.domain.purchase.order.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.common.embeddable.Address;
import com.woomoolmarket.common.embeddable.Delivery;
import com.woomoolmarket.domain.purchase.order_product.entity.OrderProduct;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.domain.purchase.product.entity.ProductCategory;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

  private Order order;
  private Product product;
  private OrderProduct orderProduct;

  @BeforeEach
  void setUp() {
    product = Product.builder()
      .price(5000)
      .stock(5000)
      .name("panda")
      .productCategory(ProductCategory.MEAT)
      .description("panda bear")
      .build();

    orderProduct = OrderProduct.builder()
      .product(product)
      .quantity(3000)
      .build();

    order = Order.builder()
      .delivery(new Delivery("panda", "010101010", new Address("1", "2", "3")))
      .orderProducts(List.of(orderProduct))
      .build();
  }

  @Test
  @DisplayName("ORDER-STATUS 기본 ONGOING")
  void orderStatusBasic() {
    assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.ONGOING);
  }

  @Test
  @DisplayName("주문 취소 시, OrderStatus -> CANCELED")
  void orderStatusAfterCancel() {
    assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.ONGOING);

    order.cancel();
    assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCELED);
  }
}