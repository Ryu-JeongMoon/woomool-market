package com.woomoolmarket.domain.purchase.product.query;

import com.querydsl.core.annotations.QueryProjection;
import com.woomoolmarket.common.enumeration.Region;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.member.query.MemberQueryResponse;
import com.woomoolmarket.domain.purchase.product.entity.ProductCategory;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductQueryResponse {

    private Long id;

    private String name;

    private String description;

    private String productImage;

    private Integer price;

    private Integer stock;

    private Status status;

    private Region region;

    private LocalDateTime createdDateTime;

    private ProductCategory productCategory;

    private MemberQueryResponse memberQueryResponse;

    @QueryProjection
    public ProductQueryResponse(Long id, String name, String description, String productImage, Integer price, Integer stock,
        Status status, Region region, LocalDateTime createdDateTime, ProductCategory productCategory, String email) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.status = status;
        this.region = region;
        this.description = description;
        this.productImage = productImage;
        this.createdDateTime = createdDateTime;
        this.productCategory = productCategory;
        this.memberQueryResponse = MemberQueryResponse.from(email);
    }
}
