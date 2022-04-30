package com.woomoolmarket.service.cart;

import com.woomoolmarket.domain.entity.enumeration.Status;
import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.repository.MemberRepository;
import com.woomoolmarket.domain.entity.Cart;
import com.woomoolmarket.domain.repository.querydto.CartQueryResponse;
import com.woomoolmarket.domain.repository.CartRepository;
import com.woomoolmarket.domain.entity.Product;
import com.woomoolmarket.domain.repository.ProductRepository;
import com.woomoolmarket.service.cart.dto.request.CartRequest;
import com.woomoolmarket.service.cart.dto.response.CartResponse;
import com.woomoolmarket.service.cart.mapper.CartResponseMapper;
import com.woomoolmarket.util.constants.ExceptionMessages;
import java.util.Collection;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

  private final CartRepository cartRepository;
  private final MemberRepository memberRepository;
  private final ProductRepository productRepository;
  private final CartResponseMapper cartResponseMapper;

  @Transactional(readOnly = true)
  public CartResponse findBy(Long cartId) {
    return cartRepository.findById(cartId)
      .map(cartResponseMapper::toDto)
      .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.Cart.NOT_FOUND));
  }

  @Transactional(readOnly = true)
  public Page<CartQueryResponse> findPickedBy(Collection<Long> cartIds, Pageable pageable) {
    return cartRepository.findPickedBy(cartIds, pageable);
  }

  @Transactional(readOnly = true)
  public Page<CartQueryResponse> searchBy(Long memberId, Pageable pageable) {
    return cartRepository.searchBy(memberId, pageable);
  }

  @Transactional
  public Long add(CartRequest cartRequest) {
    Member member = memberRepository.findByIdAndStatus(cartRequest.getMemberId(), Status.ACTIVE)
      .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.Member.NOT_FOUND));

    Product product = productRepository.findByIdAndStatus(cartRequest.getProductId(), Status.ACTIVE)
      .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.Product.NOT_FOUND));

    Cart cart = Cart.builder()
      .member(member)
      .product(product)
      .quantity(cartRequest.getQuantity())
      .build();

    return cartRepository.save(cart).getId();
  }

  @Transactional
  public void remove(Long cartId) {
    cartRepository.findById(cartId)
      .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.Cart.NOT_FOUND));

    cartRepository.deleteById(cartId);
  }

  @Transactional
  public void removeAll(Long memberId) {
    Member member = memberRepository.findById(memberId)
      .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.Member.NOT_FOUND));

    cartRepository.deleteByMember(member);
  }


  /* FOR ADMIN */
  @Transactional(readOnly = true)
  public Page<CartQueryResponse> searchForAdminBy(Pageable pageable) {
    return cartRepository.searchForAdminBy(pageable);
  }
}
