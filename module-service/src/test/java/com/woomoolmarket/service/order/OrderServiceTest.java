package com.woomoolmarket.service.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.woomoolmarket.common.enumeration.Region;
import com.woomoolmarket.domain.member.entity.Address;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.domain.purchase.cart.entity.Cart;
import com.woomoolmarket.domain.purchase.cart.repository.CartRepository;
import com.woomoolmarket.domain.purchase.order.entity.OrderStatus;
import com.woomoolmarket.domain.purchase.order.repository.OrderRepository;
import com.woomoolmarket.domain.purchase.order.repository.OrderSearchCondition;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.domain.purchase.product.entity.ProductCategory;
import com.woomoolmarket.domain.purchase.product.repository.ProductRepository;
import com.woomoolmarket.service.order.dto.response.OrderResponse;
import com.woomoolmarket.service.order.mapper.OrderResponseMapper;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Transactional
@SpringBootTest
class OrderServiceTest {

    private static Long CART_ID;
    private static Long MEMBER_ID;
    private static Long PRODUCT_ID;
    @Autowired
    EntityManager em;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderResponseMapper orderResponseMapper;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CartRepository cartRepository;

    @BeforeEach
    void init() {
        em.createNativeQuery("ALTER TABLE ORDERS ALTER COLUMN `order_id` RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE MEMBER ALTER COLUMN `member_id` RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE PRODUCT ALTER COLUMN `product_id` RESTART WITH 1").executeUpdate();

        Member member = memberRepository.save(Member.builder()
            .email("panda@naver.com")
            .password("1234")
            .address(new Address("seoul", "kyeongi", "daegu"))
            .build());
        MEMBER_ID = member.getId();

        Product product = productRepository.save(Product.builder()
            .member(member)
            .name("bear")
            .price(15000)
            .region(Region.JEJUDO)
            .description("제주도곰")
            .stock(500)
            .productCategory(ProductCategory.MEAT)
            .build());
        PRODUCT_ID = product.getId();

        CART_ID = cartRepository.save(Cart.builder()
            .member(member)
            .product(product)
            .quantity(5)
            .build()).getId();
    }

    @Test
    @DisplayName("단건 주문")
    void orderOneTest() {
        orderService.orderOne(MEMBER_ID, PRODUCT_ID, 3);
        assertThat(orderRepository.findById(1L)).isNotNull();
    }

    @Test
    @DisplayName("다건 주문")
    void orderMultipleTest() {
        orderService.orderMultiples(MEMBER_ID);
        assertThat(orderRepository.findById(1L)).isNotNull();
    }

    @Test
    @DisplayName("재고 이상 주문 불가")
    void orderOverTheStockTest() {
        assertThrows(IllegalArgumentException.class, () -> orderService.orderOne(MEMBER_ID, PRODUCT_ID, 501));
    }

    @Test
    @DisplayName("없는 상품 주문 불가")
    void orderNonExistProductTest() {
        assertThrows(EntityNotFoundException.class, () -> orderService.orderOne(MEMBER_ID, 5L, 30));
    }

    @Test
    @DisplayName("주문 내역 조회")
    void findOrdersByMemberIdTest() {
        assertThat(orderService.getListByMemberId(MEMBER_ID)).isNotNull();
    }

    @Test
    @DisplayName("주문 검색")
    void getListBySearchConditionForAdmin() {
        orderService.orderOne(MEMBER_ID, PRODUCT_ID, 3);

        OrderSearchCondition condition = OrderSearchCondition.builder()
            .orderStatus(OrderStatus.ONGOING)
            .build();

        List<OrderResponse> orderResponses = orderService.getListBySearchCondition(condition);
        assertThat(orderResponses.size()).isEqualTo(1);
    }
}