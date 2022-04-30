package com.woomoolmarket.service.member.dto.response;

import com.woomoolmarket.domain.entity.embeddable.Address;
import com.woomoolmarket.domain.entity.enumeration.Status;
import com.woomoolmarket.domain.entity.enumeration.AuthProvider;
import com.woomoolmarket.domain.entity.enumeration.Role;
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

  private Role role;
  private Address address;
  private AuthProvider authProvider;
  private Status status;

  public static MemberResponse from(String email) {
    return MemberResponse.builder()
      .email(email)
      .build();
  }
}
