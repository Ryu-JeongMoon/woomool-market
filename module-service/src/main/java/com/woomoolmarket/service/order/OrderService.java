package com.woomoolmarket.service.order;

import com.woomoolmarket.common.embeddable.Delivery;
import com.woomoolmarket.common.constant.ExceptionConstants;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.domain.purchase.cart.repository.CartRepository;
import com.woomoolmarket.domain.purchase.order.entity.Order;
import com.woomoolmarket.domain.purchase.order.repository.OrderRepository;
import com.woomoolmarket.domain.purchase.order.repository.OrderSearchCondition;
import com.woomoolmarket.domain.purchase.order_product.entity.OrderProduct;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.domain.purchase.product.repository.ProductRepository;
import com.woomoolmarket.domain.purchase.order.dto.request.OrderRequest;
import com.woomoolmarket.domain.purchase.order.dto.response.OrderResponse;
import com.woomoolmarket.service.order.mapper.OrderResponseMapper;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final OrderResponseMapper orderResponseMapper;

    public List<OrderResponse> getListByMemberId(Long memberId) {
        return orderRepository.findByMemberId(memberId)
            .stream()
            .map(orderResponseMapper::toDto)
            .collect(Collectors.toList());
    }

    public void order(OrderRequest orderRequest) {
        if (orderRequest.getProductId() != null) {
            orderMultiples(orderRequest);
        } else {
            orderOne(orderRequest);
        }
    }

    // 단건 주문
    @Transactional
    public void orderOne(OrderRequest orderRequest) {
        Member member = memberRepository.findById(orderRequest.getMemberId())
            .orElseThrow(() -> new UsernameNotFoundException(ExceptionConstants.MEMBER_NOT_FOUND));

        Product product = productRepository.findById(orderRequest.getProductId())
            .orElseThrow(() -> new EntityNotFoundException(ExceptionConstants.PRODUCT_NOT_FOUND));

        Delivery delivery = Delivery.builder()
            .receiver(member.getEmail())
            .address(member.getAddress())
            .phone(member.getPhone())
            .build();

        OrderProduct orderProduct = OrderProduct.createOrderProduct(product, orderRequest.getQuantity());

        Order order = Order.builder()
            .member(member)
            .delivery(delivery)
            .orderProducts(List.of(orderProduct))
            .build();

        orderRepository.save(order);
    }

    // 다건 주문, Cart 에서 넘겨 받고 주문 후 Cart 에서는 바로 삭제
    @Transactional
    public void orderMultiples(OrderRequest orderRequest) {
        Member member = memberRepository.findById(orderRequest.getMemberId())
            .orElseThrow(() -> new UsernameNotFoundException(ExceptionConstants.MEMBER_NOT_FOUND));

        Delivery delivery = Delivery.builder()
            .receiver(member.getEmail())
            .address(member.getAddress())
            .phone(member.getPhone())
            .build();

        List<OrderProduct> orderProducts = cartRepository.findByMember(member)
            .parallelStream()
            .map(cart -> OrderProduct.createOrderProduct(cart.getProduct(), cart.getQuantity()))
            .collect(Collectors.toList());

        Order order = Order.builder()
            .member(member)
            .delivery(delivery)
            .orderProducts(orderProducts).build();

        orderRepository.save(order);

        cartRepository.deleteByMember(member);
    }

    @Transactional
    public void cancel(Long orderId) {
        orderRepository.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionConstants.ORDER_NOT_FOUND))
            .cancel();
    }


    /* FOR ADMIN */
    public List<OrderResponse> getListBySearchCondition(OrderSearchCondition condition) {
        return orderRepository.findByConditionForAdmin(condition)
            .stream()
            .map(orderResponseMapper::toDto)
            .collect(Collectors.toList());
    }
}

/*
주문 완료도 기준 조건 두고 ex) 주문 48시간 이후 배송 완료
배치 처리해야할 듯
 */