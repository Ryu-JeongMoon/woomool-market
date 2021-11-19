package com.woomoolmarket.domain.purchase.order.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.woomoolmarket.common.auditing.BaseTimeEntity;
import com.woomoolmarket.common.embeddable.Delivery;
import com.woomoolmarket.common.util.ExceptionConstants;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.purchase.order_product.entity.OrderProduct;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "ORDERS")
@EqualsAndHashCode(of = "id", callSuper = false)
public class Order extends BaseTimeEntity {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.ONGOING;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JsonManagedReference
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @Embedded
    private Delivery delivery;

    @Builder
    public Order(Member member, Delivery delivery, List<OrderProduct> orderProducts) {
        this.member = member;
        this.delivery = delivery;
        this.orderProducts = orderProducts;
    }

    public void cancel() {
        if (orderStatus == OrderStatus.DELIVERED) {
            throw new IllegalArgumentException(ExceptionConstants.ORDER_CANNOT_CANCEL);
        }

        orderStatus = OrderStatus.CANCELED;
        orderProducts.forEach(OrderProduct::cancelOrder);
    }
}

/*
순환 참조 문제 해결 방법
1. @JsonIgnore 사용
2. @JsonManagedReference & @JsonBackReference 사용

두 방법 모두 순환참조를 막을 수 있는데 본질적으로는 차이가 있다.
@JsonIgnore 경우, property null 할당
@JsonManagedReference & @JsonBackReference 순환참조를 막기 위한 Annotation
 */