package com.woomoolmarket.domain.purchase.product.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.common.enumeration.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    private final int PRICE = 5000;
    private final int STOCK = 5000;
    private final String NAME = "panda";
    private final ProductCategory CATEGORY = ProductCategory.MEAT;
    private final String DESCRIPTION = "panda bear";
    private Product product;

    @BeforeEach
    void init() {
        product = Product.builder()
            .price(PRICE)
            .stock(STOCK)
            .name(NAME)
            .productCategory(CATEGORY)
            .description(DESCRIPTION)
            .build();
    }

    @Test
    @DisplayName("재고 추가")
    void increaseStock() {
        product.increaseStock(3000);
        assertThat(product.getStock()).isEqualTo(STOCK + 3000);
    }

    @Test
    @DisplayName("재고 감소")
    void decreaseStock() {
        product.decreaseStock(3000);
        assertThat(product.getStock()).isEqualTo(STOCK - 3000);
    }

    @Test
    @DisplayName("상품 삭제 - soft delete")
    void delete() {
        product.delete();
        assertThat(product.getStatus()).isEqualTo(Status.INACTIVE);
        assertThat(product.getDeletedDateTime()).isNotNull();
    }

    @Test
    @DisplayName("삭제된 상품 복구")
    void restore() {
        product.delete();
        product.restore();
        assertThat(product.getStatus()).isEqualTo(Status.ACTIVE);
        assertThat(product.getDeletedDateTime()).isNull();
    }
}