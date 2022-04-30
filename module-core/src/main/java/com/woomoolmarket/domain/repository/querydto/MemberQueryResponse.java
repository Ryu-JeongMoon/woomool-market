package com.woomoolmarket.domain.repository.querydto;

import com.querydsl.core.annotations.QueryProjection;
import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.entity.embeddable.Address;
import com.woomoolmarket.domain.entity.enumeration.AuthProvider;
import com.woomoolmarket.domain.entity.enumeration.Role;
import com.woomoolmarket.domain.entity.enumeration.Status;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberQueryResponse {

  private final Long id;

  private final String email;

  private final String nickname;

  private final String profileImage;

  private final String phone;

  private final String license;

  private final LocalDateTime createdDateTime;

  private final LocalDateTime lastModifiedDateTime;

  private final LocalDateTime leaveDateTime;

  private final Role role;

  private final Address address;

  private final AuthProvider authProvider;

  private final Status status;

  @Builder
  @QueryProjection
  public MemberQueryResponse(
    Long id, String email, String nickname, String profileImage, String phone, String license,
    Address address, AuthProvider authProvider, Status status, Role role,
    LocalDateTime createdDateTime, LocalDateTime lastModifiedDateTime, LocalDateTime leaveDateTime) {

    this.id = id;
    this.email = email;
    this.phone = phone;
    this.status = status;
    this.license = license;
    this.address = address;
    this.nickname = nickname;
    this.role = role;
    this.authProvider = authProvider;
    this.profileImage = profileImage;
    this.leaveDateTime = leaveDateTime;
    this.createdDateTime = createdDateTime;
    this.lastModifiedDateTime = lastModifiedDateTime;
  }

  public static MemberQueryResponse from(String email) {
    return MemberQueryResponse.builder()
      .email(email)
      .build();
  }

  public static MemberQueryResponse of(Member member) {
    return MemberQueryResponse.builder()
      .id(member.getId())
      .email(member.getEmail())
      .nickname(member.getNickname())
      .profileImage(member.getProfileImage())
      .phone(member.getPhone())
      .license(member.getLicense())
      .createdDateTime(member.getCreatedDateTime())
      .lastModifiedDateTime(member.getLastModifiedDateTime())
      .leaveDateTime(member.getLeftAt())
      .role(member.getRole())
      .address(member.getAddress())
      .authProvider(member.getInitialAuthProvider())
      .status(member.getStatus())
      .build();
  }
}
