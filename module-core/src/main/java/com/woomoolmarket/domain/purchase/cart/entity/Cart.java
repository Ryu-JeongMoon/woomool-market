package com.woomoolmarket.domain.purchase.cart.entity;

import com.woomoolmarket.common.auditing.BaseTimeEntity;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.purchase.cart_product.entity.CartProduct;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class Cart extends BaseTimeEntity {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartProduct> cartProducts = new ArrayList<>();

    @Builder
    public Cart(Member member, List<CartProduct> cartProducts) {
        this.member = member;
        this.cartProducts = cartProducts;
    }
}
