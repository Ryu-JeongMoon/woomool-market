package com.woomoolmarket.domain.purchase.product.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.common.enumeration.Region;
import com.woomoolmarket.config.TestConfig;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.domain.purchase.product.entity.ProductCategory;
import java.util.List;
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
class ProductRepositoryImplTest {

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void init() {
        Product product1 = Product.builder()
            .name("panda")
            .description("DESC")
            .price(15000)
            .stock(500)
            .region(Region.GANGWONDO)
            .productCategory(ProductCategory.FISH)
            .build();

        Product product2 = Product.builder()
            .name("panda")
            .description("DESC")
            .price(35000)
            .stock(500)
            .region(Region.CHUNGCHEONGBUKDO)
            .productCategory(ProductCategory.FISH)
            .build();

        Product product3 = Product.builder()
            .name("panda")
            .description("DESC")
            .price(56000)
            .stock(500)
            .region(Region.GYEONGGIDO)
            .productCategory(ProductCategory.FISH)
            .build();

        productRepository.saveAll(List.of(product1, product2, product3));
    }

    @Test
    @DisplayName("범위 값 동적 쿼리 조회")
    void priceRangeTest() {
        List<Product> products = productRepository.findByPriceRange(12000, 55000);

        for (Product product : products) {
            log.info("product => {}", product.getPrice());
        }

        assertThat(products.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("모든 검색 값 동적 쿼리 조회")
    void searchByAllConditionsTest() {
        ProductSearchCondition searchCondition = ProductSearchCondition.builder()
            .name("pan")
            .category(ProductCategory.FISH)
            .build();

        List<Product> products = productRepository.findByConditionForAdmin(searchCondition);

        for (Product product : products) {
            log.info("product => {}", product.getPrice());
        }

        assertThat(products.size()).isEqualTo(3);
    }
}