package com.woomoolmarket.helper;

import com.woomoolmarket.common.embeddable.Delivery;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.purchase.order.entity.Order;
import com.woomoolmarket.domain.purchase.order.repository.OrderRepository;
import com.woomoolmarket.domain.purchase.order_product.entity.OrderProduct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderTestHelper {

    private final OrderRepository orderRepository;

    public Order createOrder(Member member, OrderProduct orderProduct) {
        Order order = Order.builder()
            .member(member)
            .orderProducts(List.of(orderProduct))
            .delivery(new Delivery(member.getEmail(), member.getPhone(), member.getAddress()))
            .build();
        return orderRepository.save(order);
    }
}
