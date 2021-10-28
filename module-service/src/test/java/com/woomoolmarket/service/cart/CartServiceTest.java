package com.woomoolmarket.service.cart;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.domain.purchase.cart.entity.Cart;
import com.woomoolmarket.domain.purchase.cart.repository.CartRepository;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.domain.purchase.product.entity.ProductCategory;
import com.woomoolmarket.domain.purchase.product.repository.ProductRepository;
import com.woomoolmarket.service.cart.dto.request.CartRequest;
import com.woomoolmarket.service.cart.dto.response.CartResponse;
import java.util.List;
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
class CartServiceTest {

    private static Long MEMBER_ID;
    private static Long PRODUCT_ID;
    private static Long CART_ID;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CartService cartService;

    @BeforeEach
    void init() {
        Member member = Member.builder()
            .email("panda")
            .nickname("bear")
            .build();

        Product product = Product.builder()
            .name("fruit")
            .member(member)
            .stock(500)
            .price(10000)
            .productCategory(ProductCategory.FRUIT)
            .build();

        Cart cart = Cart.builder()
            .member(member)
            .product(product)
            .quantity(300)
            .build();

        MEMBER_ID = memberRepository.save(member).getId();
        PRODUCT_ID = productRepository.save(product).getId();
        CART_ID = cartRepository.save(cart).getId();
    }

    @Test
    @DisplayName("장바구니 단건 조회")
    void getById() {
        CartResponse cartResponse = cartService.getById(CART_ID);

        assertThat(cartResponse.getMemberResponse().getEmail()).isEqualTo("panda");
        assertThat(cartResponse.getProductResponse().getPrice()).isEqualTo(10000);
        assertThat(cartResponse.getQuantity()).isEqualTo(300);
    }

    @Test
    @DisplayName("장바구니 다건 조회")
    void getListByMember() {
        List<CartResponse> cartResponses = cartService.getListByMember(MEMBER_ID);
        assertThat(cartResponses.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("장바구니 추가")
    void add() {
        CartRequest cartRequest = CartRequest.builder()
            .memberId(MEMBER_ID)
            .productId(PRODUCT_ID)
            .quantity(500)
            .build();

        Long cartId = cartService.add(cartRequest);
        CartResponse cartResponse = cartService.getById(cartId);
        assertThat(cartResponse).isNotNull();
    }

    @Test
    @DisplayName("장바구니 단건 삭제")
    void remove() {
        cartService.removeByCartId(CART_ID);
        List<CartResponse> cartResponses = cartService.getListByMember(MEMBER_ID);
        assertThat(cartResponses.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("장바구니 다건 삭제")
    void removeAll() {
        cartService.removeAllByMemberId(MEMBER_ID);
        List<CartResponse> cartResponses = cartService.getListByMember(MEMBER_ID);
        assertThat(cartResponses.size()).isEqualTo(0);
    }
}