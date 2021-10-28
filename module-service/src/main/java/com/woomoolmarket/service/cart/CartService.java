package com.woomoolmarket.service.cart;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.common.util.ExceptionUtil;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.domain.purchase.cart.entity.Cart;
import com.woomoolmarket.domain.purchase.cart.repository.CartRepository;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.domain.purchase.product.repository.ProductRepository;
import com.woomoolmarket.service.cart.dto.request.CartRequest;
import com.woomoolmarket.service.cart.dto.response.CartResponse;
import com.woomoolmarket.service.cart.mapper.CartResponseMapper;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final CartResponseMapper cartResponseMapper;

    @Transactional(readOnly = true)
    public CartResponse getById(Long cartId) {
        return cartRepository.findById(cartId)
            .map(cartResponseMapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.CART_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<CartResponse> getListByMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.MEMBER_NOT_FOUND));

        return cartRepository.findByMember(member)
            .stream()
            .map(cartResponseMapper::toDto)
            .collect(Collectors.toList());
    }

    public Long add(CartRequest cartRequest) {
        Member member = memberRepository.findByIdAndStatus(cartRequest.getMemberId(), Status.ACTIVE)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.MEMBER_NOT_FOUND));

        Product product = productRepository.findByIdAndStatus(cartRequest.getProductId(), Status.ACTIVE)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.PRODUCT_NOT_FOUND));

        Cart cart = Cart.builder()
            .member(member)
            .product(product)
            .quantity(cartRequest.getQuantity())
            .build();

        return cartRepository.save(cart).getId();
    }

    public void removeByCartId(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.CART_NOT_FOUND));

        cartRepository.delete(cart);
    }

    public void removeAllByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.MEMBER_NOT_FOUND));

        cartRepository.deleteByMember(member);
    }
}
