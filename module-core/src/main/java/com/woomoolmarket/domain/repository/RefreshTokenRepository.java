package com.woomoolmarket.domain.repository;

import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

  @Query("select r from RefreshToken r join fetch r.member where r.member.email = :email")
  Optional<RefreshToken> findByEmail(String email);

  @Query("select r from RefreshToken r join fetch r.member where r.member = :member")
  Optional<RefreshToken> findByMember(Member member);

  @Query("select r from RefreshToken r join fetch r.member where r.tokenValue = :tokenValue")
  Optional<RefreshToken> findByTokenValue(String tokenValue);
}
