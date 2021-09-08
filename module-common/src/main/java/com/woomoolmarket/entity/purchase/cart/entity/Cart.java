package com.woomoolmarket.entity.purchase.cart.entity;

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
@EqualsAndHashCode(of = "cart_id", callSuper = false)
public class Cart extends BaseEntity {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Delivery delivery;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = LAZY)
    private Member member;

    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = LAZY)
    private Product product;


}
