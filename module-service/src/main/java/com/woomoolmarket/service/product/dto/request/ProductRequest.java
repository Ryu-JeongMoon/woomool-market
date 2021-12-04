package com.woomoolmarket.service.product.dto.request;

import com.woomoolmarket.common.enumeration.Region;
import com.woomoolmarket.domain.purchase.product.entity.ProductCategory;
import javax.persistence.Lob;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
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

    @NotBlank
    @Pattern(regexp = "^[\\w ]{4,24}$", message = "상품 이름은 6 - 24자 사이로 입력 가능합니다")
    private String name;

    @Email
    @NotBlank
    @Size(min = 9, max = 64)
    @Pattern(regexp = "(?i)^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$")
    private String email;

    @Lob
    @NotBlank
    private String description;

    @Size(max = 255)
    @Pattern(regexp = "^[\\w]*$", message = "파일 이름은 255자까지 입력 가능합니다")
    private String productImage;

    @Min(value = 1000L)
    private Integer price;

    @Min(value = 100L)
    private Integer stock;

    private Region region;

    private ProductCategory productCategory;
}
