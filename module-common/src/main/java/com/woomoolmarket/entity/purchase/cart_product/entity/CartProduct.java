package com.woomoolmarket.entity.purchase.cart_product.entity;

import com.woomoolmarket.common.BaseEntity;
import com.woomoolmarket.entity.member.entity.Member;
import com.woomoolmarket.entity.purchase.product.entity.Product;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "CART_PRODUCT")
@EqualsAndHashCode(of = "cart_product_id")
public class CartProduct extends BaseEntity {

    @Id
    @Column(name = "cart_product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private int amount;
    private int quantity;
}
