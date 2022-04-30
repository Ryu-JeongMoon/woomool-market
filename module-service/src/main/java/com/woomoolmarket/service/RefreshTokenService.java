package com.woomoolmarket.service;

import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.entity.RefreshToken;
import com.woomoolmarket.domain.port.RefreshTokenPort;
import com.woomoolmarket.domain.repository.MemberRepository;
import com.woomoolmarket.domain.repository.RefreshTokenRepository;
import com.woomoolmarket.domain.repository.querydto.RefreshTokenQueryResponse;
import com.woomoolmarket.security.dto.UserPrincipal;
import com.woomoolmarket.util.constants.ExceptionMessages;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService implements RefreshTokenPort {

  private final MemberRepository memberRepository;
  private final RefreshTokenRepository refreshTokenRepository;

  @Transactional(readOnly = true)
  public RefreshTokenQueryResponse findByEmail(String email) {
    return refreshTokenRepository.findByEmail(email)
      .map(RefreshTokenQueryResponse::from)
      .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.Token.NOT_FOUND));
  }

  @Transactional(readOnly = true)
  public RefreshTokenQueryResponse findByMember(Member member) {
    return refreshTokenRepository.findByMember(member)
      .map(RefreshTokenQueryResponse::from)
      .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.Token.NOT_FOUND));
  }

  @Transactional(readOnly = true)
  public RefreshTokenQueryResponse findByTokenValue(String tokenValue) {
    return refreshTokenRepository.findByTokenValue(tokenValue)
      .map(RefreshTokenQueryResponse::from)
      .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.Token.NOT_FOUND));
  }

  @Transactional
  public void saveOrUpdate(UserPrincipal principal, String tokenValue) {
    Member member = memberRepository.findActiveByEmail(principal.getEmail())
      .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.Member.NOT_FOUND));

    RefreshToken refreshToken = refreshTokenRepository.findByEmail(member.getEmail())
      .map(token -> token.withTokenValue(tokenValue))
      .orElseGet(
        () -> RefreshToken.builder()
          .member(member)
          .tokenValue(tokenValue)
          .expiredAt(LocalDateTime.now().plusYears(1))
          .authProvider(member.getInitialAuthProvider())
          .build()
      );

    refreshTokenRepository.save(refreshToken);
  }
}
