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
import java.util.Objects;
import javax.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Transactional
@SpringBootTest
class CartServiceTest {

    @Autowired
    CartRepository cartRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CartService cartService;
    @Autowired
    EntityManager em;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    private Long MEMBER_ID;
    private Long PRODUCT_ID;
    private Long CART_ID;

    @BeforeEach
    void init() {
        Member member = Member.builder()
            .email("panda")
            .nickname("bear")
            .build();
        MEMBER_ID = memberRepository.save(member).getId();

        Product product = Product.builder()
            .name("fruit")
            .member(member)
            .stock(50000)
            .price(10000)
            .productCategory(ProductCategory.FRUIT)
            .build();
        PRODUCT_ID = productRepository.save(product).getId();

        Cart cart = Cart.builder()
            .member(member)
            .product(product)
            .quantity(300)
            .build();
        CART_ID = cartRepository.save(cart).getId();
    }

    @AfterEach
    void clear() {
        cartRepository.deleteAll();
        memberRepository.deleteAll();
        productRepository.deleteAll();

        Objects.requireNonNull(stringRedisTemplate.keys("*")).forEach(k -> stringRedisTemplate.delete(k));

        em.createNativeQuery("ALTER TABLE CART ALTER COLUMN `cart_id` RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE MEMBER ALTER COLUMN `member_id` RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE PRODUCT ALTER COLUMN `product_id` RESTART WITH 1").executeUpdate();
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
        assertThat(cartId).isNotNull();
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