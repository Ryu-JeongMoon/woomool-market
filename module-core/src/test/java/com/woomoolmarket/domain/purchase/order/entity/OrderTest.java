package com.woomoolmarket.domain.purchase.order.entity;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.woomoolmarket.domain.member.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("ORDER-STATUS 기본 ONGOING")
    void createTest() {
        Member panda = Member.builder()
            .email("panda")
            .password("1234").build();

        Order order = Order.builder()
            .member(panda)
            .build();

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.ONGOING);
    }
}