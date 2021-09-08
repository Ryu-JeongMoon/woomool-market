package com.woomoolmarket.entity.purchase.order.entity;

import com.woomoolmarket.common.BaseEntity;
import com.woomoolmarket.entity.member.entity.Member;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "ORDERS")
@EqualsAndHashCode(of = "order_id")
public class Order extends BaseEntity {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = LAZY)
    private Member member;


}
