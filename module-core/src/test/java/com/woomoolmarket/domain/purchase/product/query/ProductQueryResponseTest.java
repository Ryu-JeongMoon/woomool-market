package com.woomoolmarket.domain.purchase.product.query;

import com.woomoolmarket.config.DataJpaTestConfig;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.helper.MemberTestHelper;
import com.woomoolmarket.helper.ProductTestHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductQueryResponseTest extends DataJpaTestConfig {

    private Product product;
    private Member member;

    @BeforeEach
    void setUp() {
        Member memberRequest = MemberTestHelper.createUser();
        member = memberRepository.save(memberRequest);

        Product productRequest = ProductTestHelper.createProduct(member);
        product = productRepository.save(productRequest);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("product -> productQueryResponse 변환 성공")
    void conversionTest() {
        ProductQueryResponse productQueryResponse = ProductQueryResponse.of(product);

        Assertions.assertThat(productQueryResponse.getId()).isEqualTo(product.getId());
        Assertions.assertThat(productQueryResponse.getName()).isEqualTo(product.getName());
        Assertions.assertThat(productQueryResponse.getPrice()).isEqualTo(product.getPrice());
        Assertions.assertThat(productQueryResponse.getRegion()).isEqualTo(product.getRegion());
        Assertions.assertThat(productQueryResponse.getStatus()).isEqualTo(product.getStatus());
        Assertions.assertThat(productQueryResponse.getStock()).isEqualTo(product.getStock().intValue());
        Assertions.assertThat(productQueryResponse.getDescription()).isEqualTo(product.getDescription());
        Assertions.assertThat(productQueryResponse.getProductImage()).isEqualTo(product.getProductImage());
        Assertions.assertThat(productQueryResponse.getCreatedDateTime()).isEqualTo(product.getCreatedDateTime());
        Assertions.assertThat(productQueryResponse.getProductCategory()).isEqualTo(product.getProductCategory());
    }
}