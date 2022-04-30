package com.woomoolmarket.domain.repository.querydto;

import com.woomoolmarket.domain.entity.enumeration.AuthProvider;
import com.woomoolmarket.domain.entity.enumeration.Role;
import com.woomoolmarket.domain.entity.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSearchCondition {

  private String email;
  private String nickname;
  private String phone;
  private String license;

  private Status status;
  private Role role;
  private AuthProvider provider;

}
