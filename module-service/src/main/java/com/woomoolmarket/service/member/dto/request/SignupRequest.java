package com.woomoolmarket.service.member.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.woomoolmarket.common.constants.RegexpConstants;
import com.woomoolmarket.common.embeddable.Address;
import javax.annotation.Nullable;
import javax.validation.constraints.Email;
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
public class SignupRequest {

  @Email
  @NotBlank
  @Size(min = 9, max = 64)
  @Pattern(regexp = RegexpConstants.EMAIL)
  private String email;

  @Size(min = 4, max = 96)
  @NotBlank
  @Pattern(regexp = RegexpConstants.LETTER_ONLY)
  private String nickname;

  @Size(min = 4, max = 255)
  @NotBlank
  @Pattern(regexp = RegexpConstants.SPECIAL_LETTER_INCLUDE)
  private String password;

  @Size(min = 10, max = 10)
  @Nullable
  @Pattern(regexp = RegexpConstants.NUMBER_ONLY)
  private String license;

  private Address address;

  @JsonIgnore
  private MultipartFile multipartFile;
}

/*
@NotNull    -> null 만 허용 안 함, "", " " 허용
@NotEmpty   -> null, "" 둘다 허용 x, " " 허용
@NotBlank   -> 다 허용 안 함
이 규칙에 따라서 Validation 적용
개발 단계에서 비밀번호 4자리 / 운영 단계에서 6 or 8 자리로 교체
 */