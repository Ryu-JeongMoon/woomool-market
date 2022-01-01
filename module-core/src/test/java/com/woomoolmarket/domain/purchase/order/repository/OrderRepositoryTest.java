package com.woomoolmarket.domain.purchase.order.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.config.DataJpaTestConfig;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.domain.purchase.order.entity.Order;
import com.woomoolmarket.domain.purchase.order.entity.OrderStatus;
import com.woomoolmarket.domain.purchase.order.query.OrderQueryResponse;
import com.woomoolmarket.domain.purchase.order_product.entity.OrderProduct;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.domain.purchase.product.repository.ProductRepository;
import com.woomoolmarket.helper.ProductTestHelper;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Log4j2
class OrderRepositoryTest extends DataJpaTestConfig {

    private static Long MEMBER_ID;
    private static Long ORDER_ID;

    @BeforeEach
    void init() {
        Member member = Member.builder()
            .email("cleancode")
            .nickname("bear")
            .password("1234")
            .phone("01012345678")
            .build();

        Member member1 = Member.builder()
            .email("testdriven")
            .nickname("coke")
            .password("1234")
            .phone("01012345678")
            .build();

        Member member2 = Member.builder()
            .email("effectivejava")
            .nickname("blue")
            .password("1234")
            .phone("01012345678")
            .build();

        MEMBER_ID = memberRepository.save(member).getId();
        memberRepository.save(member1);
        memberRepository.save(member2);

        Product product = ProductTestHelper.createProduct(member);
        productRepository.save(product);

        OrderProduct orderProduct1 = OrderProduct.createOrderProduct(product, 5000);
        OrderProduct orderProduct2 = OrderProduct.createOrderProduct(product, 3000);

        Order order = Order.builder()
            .member(member)
            .orderProducts(List.of(orderProduct1, orderProduct2))
            .build();

        ORDER_ID = orderRepository.save(order).getId();
    }

    @Test
    @DisplayName("주문 상태 코드로 조회")
    void findByConditionForAdmin_Status() {
        OrderSearchCondition condition = OrderSearchCondition.builder()
            .orderStatus(OrderStatus.ONGOING)
            .build();
        Page<OrderQueryResponse> orderQueryResponses = orderRepository.searchForAdminBy(condition, Pageable.ofSize(10));
        OrderQueryResponse order = orderQueryResponses.getContent().get(0);
        System.out.println("order.getOrderProducts() = " + order.getOrderProducts());
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.ONGOING);
    }

    @Test
    @DisplayName("회원 번호로 조회")
    void findByConditionForAdmin_Id() {
        OrderSearchCondition condition = OrderSearchCondition.builder()
            .memberId(MEMBER_ID)
            .build();

        Page<OrderQueryResponse> orderQueryResponses = orderRepository.searchForAdminBy(condition, Pageable.ofSize(10));
        assertThat(orderQueryResponses.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("이메일로 조회")
    void findByEmail() {
        OrderSearchCondition condition = OrderSearchCondition.builder()
            .email("clea")
            .build();

        Page<OrderQueryResponse> orderQueryResponses = orderRepository.searchForAdminBy(condition, Pageable.ofSize(10));
        assertThat(orderQueryResponses.getTotalElements()).isEqualTo(1);
    }
}