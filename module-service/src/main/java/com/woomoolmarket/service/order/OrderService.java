package com.woomoolmarket.service.order;

import com.woomoolmarket.common.constants.ExceptionConstants;
import com.woomoolmarket.common.embeddable.Delivery;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.domain.purchase.cart.repository.CartRepository;
import com.woomoolmarket.domain.purchase.order.entity.Order;
import com.woomoolmarket.domain.purchase.order.query.OrderQueryResponse;
import com.woomoolmarket.domain.purchase.order.repository.OrderRepository;
import com.woomoolmarket.domain.purchase.order.repository.OrderSearchCondition;
import com.woomoolmarket.domain.purchase.order_product.entity.OrderProduct;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.domain.purchase.product.repository.ProductRepository;
import com.woomoolmarket.service.order.dto.request.OrderRequest;
import com.woomoolmarket.service.order.mapper.OrderResponseMapper;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final OrderResponseMapper orderResponseMapper;

    @Transactional(readOnly = true)
    public Page<OrderQueryResponse> searchBy(Long memberId, Pageable pageable) {
        return orderRepository.searchBy(memberId, pageable);
    }

    @Transactional
    public void order(OrderRequest orderRequest) {
        if (orderRequest.getProductId() != null) {
            orderMultiples(orderRequest);
        } else {
            orderOne(orderRequest);
        }
    }

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
    @Transactional(readOnly = true)
    public Page<OrderQueryResponse> searchForAdminBy(OrderSearchCondition condition, Pageable pageable) {
        return orderRepository.searchForAdminBy(condition, pageable);
    }
}