package com.woomoolmarket.service.product.dto.request;

import com.woomoolmarket.common.enumeration.Region;
import com.woomoolmarket.domain.purchase.product.entity.ProductCategory;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @Pattern(regexp = "^[\\w]{4,24}$", message = "상품 이름은 6 - 24자 사이로 입력 가능합니다")
    private String name;

    @Pattern(regexp = "^[\\w]{4,24}$", message = "이름은 6 - 24자 사이로 입력 가능합니다")
    private String seller;

    @Lob
    private String description;

    @Size(max = 255)
    @Pattern(regexp = "^[\\w]*$", message = "파일 이름은 255자지 입력 가능합니다")
    private String productImage;

    @NotNull(message = "숫자만 입력 가능합니다")
    private Integer price;

    @NotNull(message = "숫자만 입력 가능합니다")
    private Integer stock;

    private ProductCategory productCategory;
    private Region region;
}
