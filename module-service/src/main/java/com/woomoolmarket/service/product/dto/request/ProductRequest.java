package com.woomoolmarket.service.product.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.woomoolmarket.util.constants.RegexpConstants;
import com.woomoolmarket.domain.enumeration.Region;
import com.woomoolmarket.domain.purchase.product.entity.ProductCategory;
import java.util.List;
import javax.persistence.Lob;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProductRequest {

  @NotBlank
  @Size(min = 4, max = 96)
  @Pattern(regexp = RegexpConstants.SPECIAL_LETTER_INCLUDE, message = "상품 이름은 1 - 24자 사이로 입력 가능합니다")
  private String name;

  @Email
  @NotBlank
  @Size(min = 9, max = 64)
  @Pattern(regexp = RegexpConstants.EMAIL)
  private String email;

  @Lob
  @NotBlank
  private String description;

  @Size(max = 255)
  @Pattern(regexp = RegexpConstants.LETTER_AND_NUMBER, message = "파일 이름은 63자까지 입력 가능합니다")
  private String productImage;

  @Min(value = 1000L)
  private Integer price;

  @Min(value = 100L)
  private Integer stock;

  private Region region;

  private ProductCategory productCategory;

  @JsonIgnore
  private List<MultipartFile> multipartFiles;
}
