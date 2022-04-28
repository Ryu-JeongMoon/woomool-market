package com.woomoolmarket.service.order;

import com.woomoolmarket.util.constants.ExceptionConstants;
import com.woomoolmarket.domain.embeddable.Delivery;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.domain.purchase.cart.repository.CartRepository;
import com.woomoolmarket.domain.purchase.order.entity.Order;
import com.woomoolmarket.domain.purchase.order.query.OrderQueryResponse;
import com.woomoolmarket.domain.purchase.order.repository.OrderRepository;
import com.woomoolmarket.domain.purchase.order.repository.OrderSearchCondition;
import com.woomoolmarket.domain.purchase.order_product.entity.OrderProduct;
import com.woomoolmarket.service.order.dto.request.OrderRequest;
import java.util.List;
import java.util.Objects;
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

  @Transactional(readOnly = true)
  public Page<OrderQueryResponse> searchBy(Long memberId, Pageable pageable) {
    return orderRepository.searchBy(memberId, pageable);
  }

  @Transactional
  public void order(OrderRequest orderRequest) {
    Member member = memberRepository.findById(orderRequest.getMemberId())
      .orElseThrow(() -> new UsernameNotFoundException(ExceptionConstants.MEMBER_NOT_FOUND));

    List<OrderProduct> orderProducts = cartRepository.findByIds(orderRequest.getCartIds())
      .stream()
      .filter(Objects::nonNull)
      .map(cart -> OrderProduct.createBy(cart.getProduct(), cart.getQuantity()))
      .collect(Collectors.toList());

    Delivery delivery = Delivery.createBy(member);

    Order order = Order.builder()
      .member(member)
      .delivery(delivery)
      .orderProducts(orderProducts)
      .build();

    orderRepository.save(order);

    cartRepository.deleteByIds(orderRequest.getCartIds());
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