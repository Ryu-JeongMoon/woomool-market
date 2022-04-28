package com.woomoolmarket.domain.embeddable;

import com.woomoolmarket.domain.member.entity.Member;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {

  private String phone;

  private String receiver;

  @Embedded
  private Address address;

  public static Delivery createBy(Member member) {
    return Delivery.builder()
      .receiver(member.getEmail())
      .address(member.getAddress())
      .phone(member.getPhone())
      .build();
  }
}
