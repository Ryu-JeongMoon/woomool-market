package com.woomoolmarket.service.order;

import static org.springframework.beans.support.PagedListHolder.DEFAULT_MAX_LINKED_PAGES;
import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;

import com.woomoolmarket.common.embeddable.Delivery;
import com.woomoolmarket.common.util.ExceptionUtil;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.domain.purchase.order.entity.Order;
import com.woomoolmarket.domain.purchase.order.entity.OrderStatus;
import com.woomoolmarket.domain.purchase.order.repository.OrderRepository;
import com.woomoolmarket.domain.purchase.order_product.entity.OrderProduct;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.domain.purchase.product.repository.ProductRepository;
import com.woomoolmarket.exception.product.ProductNameNotFoundException;
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
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    private final OrderResponseMapper orderResponseMapper;
    private final ModifyOrderRequestMapper modifyOrderRequestMapper;

    @Transactional
    public Long order(Long memberId, Long productId, int quantity) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new UsernameNotFoundException(ExceptionUtil.USER_NOT_FOUND));

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNameNotFoundException(ExceptionUtil.PRODUCT_NOT_FOUND));

        Delivery delivery = Delivery.builder()
            .receiver(member.getNickname())
            .address(member.getAddress())
            .phone(member.getPhone())
            .build();

        OrderProduct orderProduct = OrderProduct.createOrderProduct(product, product.getPrice(), quantity);

        Order order = Order.builder()
            .member(member)
            .delivery(delivery)
            .orderProducts(List.of(orderProduct))
            .build();

        return orderRepository.save(order).getId();
    }

    public OrderResponse findOrder(Long id) {
        return orderRepository.findById(id)
            .map(orderResponseMapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.ORDER_NOT_FOUND));
    }

    public Page<OrderResponse> findAllOrders(
        @PageableDefault(page = DEFAULT_MAX_LINKED_PAGES, size = DEFAULT_PAGE_SIZE) Pageable pageRequest) {
        return new PageImpl<>(orderRepository.findAll(pageRequest)
            .stream()
            .map(orderResponseMapper::toDto)
            .collect(Collectors.toList()));
    }

    public Page<OrderResponse> findOrdersByOrderStatus(OrderStatus orderStatus,
        @PageableDefault(page = DEFAULT_MAX_LINKED_PAGES, size = DEFAULT_PAGE_SIZE) Pageable pageRequest) {
        return new PageImpl<>(orderRepository.findOrdersByOrderStatus(pageRequest, orderStatus)
            .stream()
            .map(orderResponseMapper::toDto)
            .collect(Collectors.toList()));
    }

    @Transactional
    public OrderResponse editOrder(Long id, ModifyOrderRequest modifyOrderRequest) {
        return orderRepository.findById(id)
            .map(orderResponseMapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 주문입니다"));
    }

    @Transactional
    public void cancelOrder(Long id) {
        orderRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 주문입니다"))
            .cancel();
    }
}
