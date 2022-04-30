package com.woomoolmarket.domain.entity;

import com.woomoolmarket.domain.entity.auditing.BaseEntity;
import com.woomoolmarket.domain.entity.enumeration.AuthProvider;
import com.woomoolmarket.util.constants.ColumnConstants;
import com.woomoolmarket.util.constants.JpaConstants;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class RefreshToken extends BaseEntity {

  @Id
  @Column(name = ColumnConstants.Name.TOKEN_ID, length = ColumnConstants.Length.ID)
  @GeneratedValue(generator = JpaConstants.UUID2)
  @GenericGenerator(name = JpaConstants.UUID2, strategy = JpaConstants.UUID2_GENERATOR)
  private String id;

  @OneToOne(fetch = FetchType.LAZY, optional = false, orphanRemoval = true)
  @JoinColumn(name = ColumnConstants.Name.MEMBER_ID)
  private Member member;

  @Column(name = ColumnConstants.Name.TOKEN_VALUE, nullable = false, length = ColumnConstants.Length.REFRESH_TOKEN_MAX)
  private String tokenValue;

  @Column(name = ColumnConstants.Name.EXPIRED_AT)
  private LocalDateTime expiredAt;

  @EqualsAndHashCode.Include
  @Enumerated(EnumType.STRING)
  @Column(name = ColumnConstants.Name.AUTH_PROVIDER, length = ColumnConstants.Length.DEFAULT_STRING, nullable = false)
  private AuthProvider authProvider;

  @Builder
  public RefreshToken(Member member, LocalDateTime expiredAt, String tokenValue, AuthProvider authProvider) {
    this.member = member;
    this.expiredAt = expiredAt;
    this.tokenValue = tokenValue;
    this.authProvider = authProvider;
  }

  public void changeTokenValue(String tokenValue) {
    this.tokenValue = tokenValue;
  }

  public RefreshToken withTokenValue(String tokenValue) {
    this.tokenValue = tokenValue;
    return this;
  }
}