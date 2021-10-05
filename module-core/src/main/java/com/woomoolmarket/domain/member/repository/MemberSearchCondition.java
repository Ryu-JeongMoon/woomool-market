package com.woomoolmarket.domain.member.repository;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.member.entity.AuthProvider;
import com.woomoolmarket.domain.member.entity.Authority;
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
    private Authority authority;
    private AuthProvider provider;

}
