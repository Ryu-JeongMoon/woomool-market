package com.woomoolmarket.service.member.dto.response;

import com.woomoolmarket.domain.embeddable.Address;
import com.woomoolmarket.domain.enumeration.Status;
import com.woomoolmarket.domain.member.entity.AuthProvider;
import com.woomoolmarket.domain.member.entity.Authority;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MemberResponse {

  private Long id;

  private String email;
  private String nickname;

  private String profileImage;
  private String phone;
  private String license;

  private LocalDateTime createdDateTime;
  private LocalDateTime lastModifiedDateTime;
  private LocalDateTime leaveDateTime;

  private Authority authority;
  private Address address;
  private AuthProvider authProvider;
  private Status status;

  public static MemberResponse from(String email) {
    return MemberResponse.builder()
      .email(email)
      .build();
  }
}
