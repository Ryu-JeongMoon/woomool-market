package com.woomoolmarket.entity.purchase.order_product.entity;

import com.woomoolmarket.common.BaseEntity;
import com.woomoolmarket.entity.purchase.order.entity.Order;
import com.woomoolmarket.entity.purchase.product.entity.Product;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "ORDER_PRODUCT")
@EqualsAndHashCode(of = "order_product_id")
public class OrderProduct extends BaseEntity {

    @Id
    @Column(name = "order_product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private int orderAmount;
    private int orderQuantity;
}
