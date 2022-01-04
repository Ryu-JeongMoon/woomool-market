package com.woomoolmarket.domain.member.query;

import com.querydsl.core.annotations.QueryProjection;
import com.woomoolmarket.common.embeddable.Address;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.member.entity.AuthProvider;
import com.woomoolmarket.domain.member.entity.Authority;
import com.woomoolmarket.domain.member.entity.Member;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberQueryResponse implements Serializable {

  private final Long id;

  private final String email;

  private final String nickname;

  private final String profileImage;

  private final String phone;

  private final String license;

  private final LocalDateTime createdDateTime;

  private final LocalDateTime lastModifiedDateTime;

  private final LocalDateTime leaveDateTime;

  private final Authority authority;

  private final Address address;

  private final AuthProvider authProvider;

  private final Status status;

  @Builder
  @QueryProjection
  public MemberQueryResponse(
    Long id, String email, String nickname, String profileImage, String phone, String license,
    Address address, AuthProvider authProvider, Status status, Authority authority,
    LocalDateTime createdDateTime, LocalDateTime lastModifiedDateTime, LocalDateTime leaveDateTime) {

    this.id = id;
    this.email = email;
    this.phone = phone;
    this.status = status;
    this.license = license;
    this.address = address;
    this.nickname = nickname;
    this.authority = authority;
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
      .leaveDateTime(member.getLeaveDateTime())
      .authority(member.getAuthority())
      .address(member.getAddress())
      .authProvider(member.getAuthProvider())
      .status(member.getStatus())
      .build();
  }
}
