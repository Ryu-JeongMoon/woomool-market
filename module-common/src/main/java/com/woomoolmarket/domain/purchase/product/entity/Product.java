package com.woomoolmarket.domain.purchase.product.entity;

import com.woomoolmarket.common.BaseTimeEntity;
import com.woomoolmarket.common.Region;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@EqualsAndHashCode(of = "product_id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseTimeEntity {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String seller;

    private Integer price;

    private Integer stock;

    private String description;

    private String productImage;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Region region;

    @Builder
    public Product(String seller, String name, Integer price, Integer stock, String description, String productImg,
        Category category, Region region) {
        this.seller = seller;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.productImage = productImg;
        this.category = category;
        this.region = region;
    }
}
