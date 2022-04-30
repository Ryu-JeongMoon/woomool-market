package com.woomoolmarket.service.order;

import com.woomoolmarket.domain.entity.embeddable.Delivery;
import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.repository.MemberRepository;
import com.woomoolmarket.domain.repository.CartRepository;
import com.woomoolmarket.domain.entity.Order;
import com.woomoolmarket.domain.repository.querydto.OrderQueryResponse;
import com.woomoolmarket.domain.repository.OrderRepository;
import com.woomoolmarket.domain.repository.querydto.OrderSearchCondition;
import com.woomoolmarket.domain.entity.OrderProduct;
import com.woomoolmarket.service.order.dto.request.OrderRequest;
import com.woomoolmarket.util.constants.ExceptionMessages;
import java.util.List;
import java.util.Objects;
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
      .orElseThrow(() -> new UsernameNotFoundException(ExceptionMessages.Member.NOT_FOUND));

    List<OrderProduct> orderProducts = cartRepository.findByIds(orderRequest.getCartIds())
      .stream()
      .filter(Objects::nonNull)
      .map(cart -> OrderProduct.createBy(cart.getProduct(), cart.getQuantity()))
      .toList();

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
      .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.Order.NOT_FOUND))
      .cancel();
  }


  /* FOR ADMIN */
  @Transactional(readOnly = true)
  public Page<OrderQueryResponse> searchForAdminBy(OrderSearchCondition condition, Pageable pageable) {
    return orderRepository.searchForAdminBy(condition, pageable);
  }
}