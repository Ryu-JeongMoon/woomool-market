package com.woomoolmarket.domain.purchase.order.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.config.TestConfig;
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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void init() {
        ProductTestHelper productTestHelper = new ProductTestHelper(productRepository);

        Member member = Member.builder()
            .email("cleancode")
            .nickname("bear")
            .password("1234")
            .build();

        Member member1 = Member.builder()
            .email("testdriven")
            .nickname("coke")
            .password("1234")
            .build();

        Member member2 = Member.builder()
            .email("effectivejava")
            .nickname("blue")
            .password("1234")
            .build();

        MEMBER_ID = memberRepository.save(member).getId();
        memberRepository.save(member1);
        memberRepository.save(member2);

        Product product = productTestHelper.createProduct(member);
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