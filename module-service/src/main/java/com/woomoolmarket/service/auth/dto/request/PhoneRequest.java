package com.woomoolmarket.service.auth.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneRequest {

  @NotBlank
  @Pattern(regexp = "^01(\\d{8,9})$")
  private String phone;
}
