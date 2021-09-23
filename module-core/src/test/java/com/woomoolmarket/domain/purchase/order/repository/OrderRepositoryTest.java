package com.woomoolmarket.domain.purchase.order.repository;

import com.woomoolmarket.config.TestConfig;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.domain.purchase.order.entity.Order;
import com.woomoolmarket.domain.purchase.order.entity.OrderStatus;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;

@Log4j2
@DataJpaTest
@Import(TestConfig.class)
class OrderRepositoryTest {

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

        Order order = Order.builder()
            .member(member)
            .build();

        memberRepository.save(member);
        orderRepository.save(order);
    }

    @Test
    void findOrdersTest() {
        List<Order> orders = orderRepository.findOrdersByOrderStatus(Pageable.unpaged(), OrderStatus.ONGOING);

        for (Order order1 : orders) {
            System.out.println("order1.getId() = " + order1.getId());
            System.out.println("order1.getMember().getEmail() = " + order1.getMember().getEmail());
            System.out.println("order1.getOrderStatus() = " + order1.getOrderStatus());
        }
    }
}