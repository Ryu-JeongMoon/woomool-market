package com.woomoolmarket.domain.purchase.order.entity;

import static javax.persistence.FetchType.LAZY;

import com.woomoolmarket.common.BaseEntity;
import com.woomoolmarket.domain.member.entity.Member;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "ORDERS")
@EqualsAndHashCode(of = "id", callSuper = false)
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
