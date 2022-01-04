package com.woomoolmarket.domain.purchase.product.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.image.entity.Image;
import com.woomoolmarket.helper.ImageTestHelper;
import java.util.Collections;
import java.util.List;
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
    assertThat(product.getStock().intValue()).isEqualTo(STOCK + 3000);
  }

  @Test
  @DisplayName("재고 감소")
  void decreaseStock() {
    product.decreaseStock(3000);
    assertThat(product.getStock().intValue()).isEqualTo(STOCK - 3000);
  }

  @Test
  @DisplayName("재고 감소 실패 - 재고 부족 IllegalArgumentException 발생")
  void decreaseStockFail() {
    int quantityOverStock = 1000000;
    assertThrows(IllegalArgumentException.class, () -> product.decreaseStock(quantityOverStock));
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

  @Test
  @DisplayName("이미지 추가 성공")
  void addImages() {
    Image image = ImageTestHelper.createImage();
    product.addImages(List.of(image));

    assertThat(product.getImages().get(0)).isNotNull();
  }

  @Test
  @DisplayName("이미지 추가 시 해당 이미지에 상품 세팅")
  void addImagesSetProduct() {
    Image image = ImageTestHelper.createImage();
    product.addImages(List.of(image));

    assertThat(image.getProduct()).isEqualTo(product);
  }

  @Test
  @DisplayName("이미지 추가 실패 - 빈 리스트")
  void addVoidImages() {
    product.addImages(Collections.emptyList());
    assertThat(product.getImages().size()).isEqualTo(0);
  }

  @Test
  @DisplayName("HashCode 테스트")
  void hashCodeTest() {
    Product newProduct = product;
    assertThat(product.hashCode()).isEqualTo(newProduct.hashCode());
  }

  @Test
  @DisplayName("equals 테스트")
  void equalsTest() {
    Product newProduct = Product.builder()
      .name(product.getName())
      .member(product.getMember())
      .productImage(product.getProductImage())
      .productCategory(product.getProductCategory())
      .price(product.getPrice())
      .stock(product.getStock().intValue())
      .region(product.getRegion())
      .build();
    assertThat(product.equals(newProduct)).isEqualTo(true);
  }
}