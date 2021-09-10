package com.woomoolmarket.model.purchase.cart_product.entity;

import static javax.persistence.FetchType.LAZY;

import com.woomoolmarket.common.BaseEntity;
import com.woomoolmarket.model.member.entity.Member;
import com.woomoolmarket.model.purchase.product.entity.Product;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
