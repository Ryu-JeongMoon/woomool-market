package com.woomoolmarket.domain.purchase.order_product.entity;


import com.woomoolmarket.common.auditing.BaseTimeEntity;
import com.woomoolmarket.domain.purchase.order.entity.Order;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ORDER_PRODUCT")
@EqualsAndHashCode(of = "id", callSuper = false)
public class OrderProduct extends BaseTimeEntity {

    @Id
    @Column(name = "order_product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private int amount;
    private int quantity;

    @Builder
    public OrderProduct(Order order, Product product, int amount, int quantity) {
        this.order = order;
        this.product = product;
        this.amount = amount;
        this.quantity = quantity;
    }

    public static OrderProduct createOrderProduct(Product product, int amount, int quantity) {
        product.decreaseStock(quantity);

        return OrderProduct.builder()
            .product(product)
            .amount(amount)
            .quantity(quantity)
            .build();
    }
}
