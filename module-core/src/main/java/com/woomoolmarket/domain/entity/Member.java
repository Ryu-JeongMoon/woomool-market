package com.woomoolmarket.domain.entity;

import static com.woomoolmarket.util.constants.ColumnConstants.Length;

import com.woomoolmarket.domain.entity.auditing.BaseEntity;
import com.woomoolmarket.domain.entity.embeddable.Address;
import com.woomoolmarket.domain.entity.enumeration.AuthProvider;
import com.woomoolmarket.domain.entity.enumeration.Role;
import com.woomoolmarket.domain.entity.enumeration.Status;
import com.woomoolmarket.util.constants.ColumnConstants;
import com.woomoolmarket.util.constants.ColumnConstants.Name;
import com.woomoolmarket.util.constants.Columns;
import com.woomoolmarket.util.constants.ExceptionMessages;
import com.woomoolmarket.util.constants.Lengths;
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
import org.apache.commons.lang3.StringUtils;

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
  @Column(name = Name.MEMBER_ID, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @EqualsAndHashCode.Include
  @Column(name = Name.EMAIL, nullable = false, length = Lengths.Member.EMAIL_MAX)
  private String email;

  @EqualsAndHashCode.Include
  @Column(name = Name.NICKNAME, nullable = false, length = Lengths.Member.NICKNAME_MAX)
  private String nickname;

  @Column(name = Name.PASSWORD, nullable = false)
  private String password;

  @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  private Image image;

  @Column(name = ColumnConstants.Name.PROFILE_IMAGE)
  private String profileImage;

  @Size(max = Lengths.Member.PHONE)
  private String phone;

  @Size(max = Lengths.Member.LICENSE)
  private String license;

  private LocalDateTime leftAt;

  @Embedded
  private Address address;

  @Enumerated(EnumType.STRING)
  @Column(name = Columns.Member.INITIAL_AUTH_PROVIDER, nullable = false)
  private AuthProvider initialAuthProvider;

  @Enumerated(EnumType.STRING)
  @Column(name = Columns.Member.LATEST_AUTH_PROVIDER)
  private AuthProvider latestAuthProvider;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = Lengths.FIFTY)
  private Role role;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = Lengths.FIFTY)
  private Status status = Status.ACTIVE;

  @Builder
  public Member(String email, String nickname, String password, Image image, String profileImage,
    String phone, String license, Address address, AuthProvider initialAuthProvider, Role role) {

    this.email = email;
    this.image = image;
    this.phone = phone;
    this.address = address;
    this.license = license;
    this.nickname = nickname;
    this.password = password;
    this.profileImage = profileImage;
    this.role = role != null ? role : Role.USER;
    this.initialAuthProvider = initialAuthProvider != null ? initialAuthProvider : AuthProvider.LOCAL;
  }

  public void changePassword(String password) {
    this.password = password;
  }

  public void changeToEncodedPassword(String encodedPassword) {
    if (StringUtils.isBlank(encodedPassword) || encodedPassword.length() != Length.ENCODED_PASSWORD)
      throw new IllegalArgumentException(ExceptionMessages.Member.PASSWORD_NOT_ENCODED);

    this.password = encodedPassword;
  }

  private void changeStatus(Status status) {
    if (status == Status.ACTIVE)
      restore();
    else
      leave();
  }

  public void leave() {
    if (this.leftAt != null)
      throw new IllegalStateException(ExceptionMessages.Member.ALREADY_LEFT);

    this.leftAt = LocalDateTime.now();
    this.status = Status.INACTIVE;
  }

  public void restore() {
    this.leftAt = null;
    this.status = Status.ACTIVE;
  }

  public void changeLatestAuthProvider(AuthProvider latestAuthProvider) {
    this.latestAuthProvider = latestAuthProvider;
  }

  public void changeAuthority(Role role) {
    this.role = role;
  }

  public Member editByOAuth2(String nickname, String profileImage, AuthProvider authProvider) {
    this.nickname = nickname;
    this.profileImage = profileImage;
    this.initialAuthProvider = authProvider;
    return this;
  }

  public void addImage(Image image) {
    if (image == null)
      return;

    this.image = image;
    image.setMember(this);
  }
}
