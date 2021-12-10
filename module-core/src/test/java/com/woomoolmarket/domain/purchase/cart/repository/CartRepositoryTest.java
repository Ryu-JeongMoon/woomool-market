package com.woomoolmarket.domain.purchase.cart.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.common.enumeration.Region;
import com.woomoolmarket.config.TestConfig;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.domain.purchase.cart.entity.Cart;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.domain.purchase.product.entity.ProductCategory;
import com.woomoolmarket.domain.purchase.product.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Log4j2
@DataJpaTest
@Import(TestConfig.class)
class CartRepositoryTest {

    private Long CART_ID;
    private Member member;
    private Product product;

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    EntityManager em;

    @BeforeEach
    void init() {
        member = Member.builder()
            .email("pandabear")
            .nickname("nickname")
            .password("1234")
            .build();

        product = Product.builder()
            .name("panda-bear-panda")
            .description("panda")
            .stock(5000)
            .price(10000)
            .region(Region.JEJUDO)
            .productCategory(ProductCategory.MEAT)
            .build();

        Cart cart = Cart.builder()
            .member(member)
            .product(product)
            .quantity(300)
            .build();

        memberRepository.save(member);
        productRepository.save(product);
        CART_ID = cartRepository.save(cart).getId();
    }

    @Test
    @DisplayName("회원 장바구니 찾기")
    void findByMember() {
        List<Cart> carts = cartRepository.findByMember(member);
        assertThat(carts.get(0).getId()).isEqualTo(CART_ID);
    }

    @Test
    @DisplayName("장바구니 단건 삭제")
    void deleteById() {
        cartRepository.deleteById(CART_ID);

        em.flush();
        em.clear();

        assertThat(cartRepository.findById(CART_ID)).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("특정 회원 장바구니 전체 삭제")
    void deleteByMember() {
        cartRepository.deleteByMember(member);

        em.flush();
        em.clear();

        assertThat(cartRepository.findById(CART_ID)).isEqualTo(Optional.empty());
    }
}