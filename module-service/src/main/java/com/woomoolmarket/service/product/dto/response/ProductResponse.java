package com.woomoolmarket.service.product.dto.response;

import com.woomoolmarket.common.enumeration.Region;
import com.woomoolmarket.domain.purchase.product.entity.ProductCategory;
import com.woomoolmarket.service.member.dto.response.MemberResponse;
import java.time.LocalDateTime;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductResponse {

    private String name;

    private String description;

    private String productImage;

    private MemberResponse memberResponse;

    private int price;

    private int stock;

    private LocalDateTime createdDateTime;

    private ProductCategory productCategory;

    private Region region;
}
