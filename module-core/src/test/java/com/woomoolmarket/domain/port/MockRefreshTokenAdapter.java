package com.woomoolmarket.domain.port;

import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.repository.querydto.RefreshTokenQueryResponse;
import com.woomoolmarket.security.dto.UserPrincipal;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("test")
@Component
public class MockRefreshTokenAdapter implements RefreshTokenPort {

  @Override
  public RefreshTokenQueryResponse findByEmail(String email) {
    return null;
  }

  @Override
  public RefreshTokenQueryResponse findByMember(Member member) {
    return null;
  }

  @Override
  public RefreshTokenQueryResponse findByTokenValue(String tokenValue) {
    return null;
  }

  @Override
  public void saveOrUpdate(UserPrincipal principal, String tokenValue) {

  }
}
