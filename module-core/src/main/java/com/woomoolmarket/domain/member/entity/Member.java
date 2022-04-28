package com.woomoolmarket.domain.member.entity;

import com.woomoolmarket.domain.auditing.BaseEntity;
import com.woomoolmarket.util.constants.Columns;
import com.woomoolmarket.util.constants.Lengths;
import com.woomoolmarket.domain.embeddable.Address;
import com.woomoolmarket.domain.enumeration.Status;
import com.woomoolmarket.domain.image.entity.Image;
import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(uniqueConstraints = {
  @UniqueConstraint(name = Columns.Member.UNIQUE_EMAIL, columnNames = Columns.Member.EMAIL)
})
public class Member extends BaseEntity {

  @Id
  @Column(name = Columns.Member.MEMBER_ID, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @EqualsAndHashCode.Include
  @Column(nullable = false, length = Lengths.Member.EMAIL_MAX)
  private String email;

  @EqualsAndHashCode.Include
  @Column(nullable = false, length = Lengths.Member.NICKNAME_MAX)
  private String nickname;

  @Column(nullable = false)
  private String password;

  @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  private Image image;

  @Column(name = Columns.Member.PROFILE_IMAGE)
  private String profileImage;

  @Size(max = Lengths.Member.PHONE)
  private String phone;

  @Size(max = Lengths.Member.LICENSE)
  private String license;

  private LocalDateTime leaveDateTime;

  @Embedded
  private Address address;

  @Enumerated(EnumType.STRING)
  @Column(name = Columns.Member.AUTH_PROVIDER, nullable = false)
  private AuthProvider authProvider;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = Lengths.FIFTY)
  private Authority authority;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = Lengths.FIFTY)
  private Status status = Status.ACTIVE;

  @Builder
  public Member(String email, String nickname, String password, Image image, String profileImage,
    String phone, String license, Address address, AuthProvider authProvider, Authority authority) {
    this.email = email;
    this.image = image;
    this.phone = phone;
    this.address = address;
    this.license = license;
    this.nickname = nickname;
    this.password = password;
    this.profileImage = profileImage;
    this.authority = authority != null ? authority : Authority.ROLE_USER;
    this.authProvider = authProvider != null ? authProvider : AuthProvider.LOCAL;
  }

  public String getAuthorityKey() {
    return this.authority.getKey();
  }

  public void changePassword(String password) {
    this.password = password;
  }

  public void changeAuthority(Authority authority) {
    this.authority = authority;
  }

  public void leave() {
    this.status = Status.INACTIVE;
    this.leaveDateTime = LocalDateTime.now();
  }

  public void restore() {
    this.status = Status.ACTIVE;
    this.leaveDateTime = null;
  }

  public Member editByOAuth2(String nickname, String profileImage, AuthProvider authProvider) {
    this.nickname = nickname;
    this.profileImage = profileImage;
    this.authProvider = authProvider;
    return this;
  }

  public void addImage(Image image) {
    if (image == null)
      return;

    this.image = image;
    image.setMember(this);
  }
}
