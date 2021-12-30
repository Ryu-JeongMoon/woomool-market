package com.woomoolmarket.service.product.dto.response;

import com.woomoolmarket.common.enumeration.Region;
import com.woomoolmarket.domain.purchase.product.entity.ProductCategory;
import com.woomoolmarket.service.member.dto.response.MemberResponse;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductResponseTest {

    private static final int NUMBER = 10000;
    private static final int PRICE = 50000;
    private static final int STOCK = 50000;

    private static final String PANDA = "PANDA";
    private static final String EMAIL = "EMAIL";
    private static final String DESCRIPTION = "PRODUCT_DESCRIPTION";
    private static final String PRODUCT_IMAGE = "PRODUCT_IMAGE";
    private static final String PRODUCT_NAME = "PRODUCT_NAME";
    private static final Region REGION = Region.CHUNGCHEONGBUKDO;
    private static final ProductCategory PRODUCT_CATEGORY = ProductCategory.CEREAL;
    private static final LocalDateTime CREATED_DATE_TIME = LocalDateTime.of(2000, 1, 1, 1, 1, 1);


    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        MemberResponse memberResponse = MemberResponse.builder()
            .email(EMAIL)
            .id(1L)
            .build();

        productResponse = ProductResponse.builder()
            .name(PRODUCT_NAME)
            .description(DESCRIPTION)
            .productImage(PRODUCT_IMAGE)
            .productCategory(PRODUCT_CATEGORY)
            .createdDateTime(CREATED_DATE_TIME)
            .price(PRICE)
            .stock(STOCK)
            .region(REGION)
            .memberResponse(memberResponse)
            .build();
    }

    @Test
    void getName() {
        Assertions.assertThat(productResponse.getName()).isEqualTo(PRODUCT_NAME);
    }

    @Test
    void getDescription() {
        Assertions.assertThat(productResponse.getDescription()).isEqualTo(DESCRIPTION);
    }

    @Test
    void getProductImage() {
        Assertions.assertThat(productResponse.getProductImage()).isEqualTo(PRODUCT_IMAGE);
    }

    @Test
    void getMemberResponse() {
        Assertions.assertThat(productResponse.getMemberResponse().getEmail()).isEqualTo(EMAIL);
    }

    @Test
    void getPrice() {
        Assertions.assertThat(productResponse.getPrice()).isEqualTo(PRICE);
    }

    @Test
    void getStock() {
        Assertions.assertThat(productResponse.getStock()).isEqualTo(STOCK);
    }

    @Test
    void getCreatedDateTime() {
        Assertions.assertThat(productResponse.getCreatedDateTime()).isEqualTo(CREATED_DATE_TIME);
    }

    @Test
    void getProductCategory() {
        Assertions.assertThat(productResponse.getProductCategory()).isEqualTo(PRODUCT_CATEGORY);
    }

    @Test
    void getRegion() {
        Assertions.assertThat(productResponse.getRegion()).isEqualTo(REGION);
    }

    @Test
    void setName() {
        productResponse.setName(PANDA);
        Assertions.assertThat(productResponse.getName()).isEqualTo(PANDA);
    }

    @Test
    void setDescription() {
        productResponse.setDescription(PANDA);
        Assertions.assertThat(productResponse.getDescription()).isEqualTo(PANDA);
    }

    @Test
    void setProductImage() {
        productResponse.setProductImage(PANDA);
        Assertions.assertThat(productResponse.getProductImage()).isEqualTo(PANDA);
    }

    @Test
    void setMemberResponse() {
        long id = 2L;
        MemberResponse memberResponse = MemberResponse.builder()
            .id(id)
            .build();
        productResponse.setMemberResponse(memberResponse);
        Assertions.assertThat(productResponse.getMemberResponse().getId()).isEqualTo(id);
    }

    @Test
    void setPrice() {
        productResponse.setPrice(NUMBER);
        Assertions.assertThat(productResponse.getPrice()).isEqualTo(NUMBER);
    }

    @Test
    void setStock() {
        productResponse.setStock(NUMBER);
        Assertions.assertThat(productResponse.getStock()).isEqualTo(NUMBER);
    }

    @Test
    void setCreatedDateTime() {
        LocalDateTime now = LocalDateTime.now();
        productResponse.setCreatedDateTime(now);
        Assertions.assertThat(productResponse.getCreatedDateTime()).isEqualTo(now);
    }

    @Test
    void setProductCategory() {
        productResponse.setProductCategory(ProductCategory.FISH);
        Assertions.assertThat(productResponse.getProductCategory()).isEqualTo(ProductCategory.FISH);
    }

    @Test
    void setRegion() {
        productResponse.setRegion(Region.JEJUDO);
        Assertions.assertThat(productResponse.getRegion()).isEqualTo(Region.JEJUDO);
    }

}