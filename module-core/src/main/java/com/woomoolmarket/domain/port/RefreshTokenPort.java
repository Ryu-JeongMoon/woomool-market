package com.woomoolmarket.domain.port;

import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.repository.querydto.RefreshTokenQueryResponse;
import com.woomoolmarket.security.dto.UserPrincipal;

public interface RefreshTokenPort {

  RefreshTokenQueryResponse findByEmail(String email);

  RefreshTokenQueryResponse findByMember(Member member);

  RefreshTokenQueryResponse findByTokenValue(String tokenValue);

  void saveOrUpdate(UserPrincipal principal, String tokenValue);
}
