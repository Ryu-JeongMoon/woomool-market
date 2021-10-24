package com.woomoolmarket.domain.purchase.order.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.config.TestConfig;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.domain.purchase.order.entity.Order;
import com.woomoolmarket.domain.purchase.order.entity.OrderStatus;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Log4j2
@DataJpaTest
@Import(TestConfig.class)
class OrderRepositoryTest {

    private static Long MEMBER_ID;
    private static Long ORDER_ID;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void init() {
        Member member = Member.builder()
            .email("panda")
            .nickname("bear")
            .password("1234")
            .build();

        Member member1 = Member.builder()
            .email("pepsi")
            .nickname("coke")
            .password("1234")
            .build();

        Member member2 = Member.builder()
            .email("horangi")
            .nickname("blue")
            .password("1234")
            .build();

        Order order = Order.builder()
            .member(member)
            .build();

        MEMBER_ID = memberRepository.save(member).getId();
        memberRepository.save(member1);
        memberRepository.save(member2);
        ORDER_ID = orderRepository.save(order).getId();
    }

    @Test
    @DisplayName("주문 상태 코드로 조회")
    void findByConditionForAdmin_Status() {
        OrderSearchCondition condition = OrderSearchCondition.builder()
            .orderStatus(OrderStatus.ONGOING)
            .build();
        Order order = orderRepository.findByConditionForAdmin(condition).get(0);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.ONGOING);
    }

    @Test
    @DisplayName("회원 번호로 조회")
    void findByConditionForAdmin_Id() {
        OrderSearchCondition condition = OrderSearchCondition.builder()
            .orderStatus(OrderStatus.ONGOING)
            .email("pan")
            .memberId(MEMBER_ID)
            .build();

        List<Order> orders = orderRepository.findByConditionForAdmin(condition);
        assertThat(orders.size()).isEqualTo(1);
    }
}