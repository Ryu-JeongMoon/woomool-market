package com.woomoolmarket.domain.embeddable;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Serializable {

  @Pattern(regexp = "^[\\w]{2,24}$")
  private String city;

  @Pattern(regexp = "^[\\w]{2,24}$")
  private String street;

  @Pattern(regexp = "^[\\d]{5,6}$")
  private String zipcode;
}
