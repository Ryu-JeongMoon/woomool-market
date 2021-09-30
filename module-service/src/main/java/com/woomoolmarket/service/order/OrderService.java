package com.woomoolmarket.service.order;

import com.woomoolmarket.common.embeddable.Delivery;
import com.woomoolmarket.common.util.ExceptionUtil;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.domain.purchase.cart.repository.CartRepository;
import com.woomoolmarket.domain.purchase.order.entity.Order;
import com.woomoolmarket.domain.purchase.order.entity.OrderStatus;
import com.woomoolmarket.domain.purchase.order.repository.OrderRepository;
import com.woomoolmarket.domain.purchase.order_product.entity.OrderProduct;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.domain.purchase.product.repository.ProductRepository;
import com.woomoolmarket.service.order.dto.request.ModifyOrderRequest;
import com.woomoolmarket.service.order.dto.response.OrderResponse;
import com.woomoolmarket.service.order.mapper.ModifyOrderRequestMapper;
import com.woomoolmarket.service.order.mapper.OrderResponseMapper;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    private final ModifyOrderRequestMapper modifyRequestMapper;

    // Controller 에서 본인만 가능하도록 권한 설정 필요
    public Page<OrderResponse> findOrdersByMemberId(Long memberId, Pageable pageRequest) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.USER_NOT_FOUND));

        return new PageImpl<>(orderRepository.findOrdersByMember(member, pageRequest)
            .stream()
            .map(orderResponseMapper::toDto)
            .collect(Collectors.toList()));
    }

    // 단건 주문
    @Transactional
    public void orderOne(Long memberId, Long productId, int quantity) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new UsernameNotFoundException(ExceptionUtil.USER_NOT_FOUND));

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.PRODUCT_NOT_FOUND));

        Delivery delivery = Delivery.builder()
            .receiver(member.getEmail())
            .address(member.getAddress())
            .phone(member.getPhone())
            .build();

        OrderProduct orderProduct = OrderProduct.createOrderProduct(product, product.getPrice(), quantity);

        Order order = Order.builder()
            .member(member)
            .delivery(delivery)
            .orderProducts(List.of(orderProduct))
            .build();

        orderRepository.save(order);
    }

    // 다건 주문, Cart 에서 넘겨 받고 주문 후 Cart 에서는 바로 삭제
    @Transactional
    public void orderMultiples(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new UsernameNotFoundException(ExceptionUtil.USER_NOT_FOUND));

        Delivery delivery = Delivery.builder()
            .receiver(member.getEmail())
            .address(member.getAddress())
            .phone(member.getPhone())
            .build();

        List<OrderProduct> orderProducts = cartRepository.findAllByMember(member)
            .parallelStream()
            .map(cart -> OrderProduct.createOrderProduct(cart.getProduct(), cart.getProduct().getPrice(), cart.getQuantity()))
            .collect(Collectors.toList());

        Order order = Order.builder()
            .member(member)
            .delivery(delivery)
            .orderProducts(orderProducts).build();

        orderRepository.save(order);

        cartRepository.deleteAllByMemberId(memberId);
    }

    @Transactional
    public OrderResponse edit(Long orderId, ModifyOrderRequest modifyRequest) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.ORDER_NOT_FOUND));
        modifyRequestMapper.updateFromDto(modifyRequest, order);
        return orderResponseMapper.toDto(order);
    }

    @Transactional
    public void cancel(Long orderId) {
        orderRepository.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.ORDER_NOT_FOUND))
            .cancel();
    }


    /* FOR ADMIN */
    public OrderResponse findOrder(Long id) {
        return orderRepository.findById(id)
            .map(orderResponseMapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.ORDER_NOT_FOUND));
    }

    public Page<OrderResponse> findAllOrders(Pageable pageRequest) {
        return new PageImpl<>(orderRepository.findAll(pageRequest)
            .stream()
            .map(orderResponseMapper::toDto)
            .collect(Collectors.toList()));
    }

    public Page<OrderResponse> findAllActiveOrders(Pageable pageRequest) {
        return new PageImpl<>(orderRepository.findAllActive(pageRequest)
            .stream()
            .map(orderResponseMapper::toDto)
            .collect(Collectors.toList()));
    }

    public Page<OrderResponse> findAllInactiveOrders(Pageable pageRequest) {
        return new PageImpl<>(orderRepository.findAllInactive(pageRequest)
            .stream()
            .map(orderResponseMapper::toDto)
            .collect(Collectors.toList()));
    }

    public Page<OrderResponse> findOrdersByOrderStatus(OrderStatus orderStatus, Pageable pageRequest) {
        return new PageImpl<>(orderRepository.findOrdersByOrderStatus(orderStatus, pageRequest)
            .stream()
            .map(orderResponseMapper::toDto)
            .collect(Collectors.toList()));
    }
}

/*
주문 완료도 기준 조건 두고 ex) 주문 48시간 이후 배송 완료
배치 처리해야할 듯
 */