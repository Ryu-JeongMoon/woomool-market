package com.woomoolmarket.domain.member.entity;

import com.woomoolmarket.common.auditing.BaseEntity;
import com.woomoolmarket.common.enumeration.Status;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

@Setter
@Getter
@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "email"}, callSuper = false)
@Table(uniqueConstraints = {@UniqueConstraint(name = "unique_email", columnNames = "email")})
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String nickname;

    private String password;

    private String profileImage;

    private String phone;

    private String license;

    private LocalDateTime leaveDateTime;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Enumerated(EnumType.STRING)
    private Authority authority = Authority.ROLE_USER;

    @Enumerated(EnumType.STRING)
    private Status memberStatus = Status.ACTIVE;

    @Builder
    public Member(String email, String nickname, String password, String profileImage,
        String phone, String license, Address address, AuthProvider provider, Authority authority) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.profileImage = profileImage;
        this.phone = phone;
        this.license = license;
        this.address = address;
        this.provider = provider;
        this.authority = authority;
    }

    public void encodePassword(String password) {
        this.password = password;
    }

    public void registerAuthority(Authority authority) {
        this.authority = authority;
    }

    public void leave() {
        changeStatusAndLeaveDateTime(Status.INACTIVE, LocalDateTime.now());
    }

    public void restore() {
        changeStatusAndLeaveDateTime(Status.ACTIVE, null);
    }

    public void changeStatusAndLeaveDateTime(Status memberStatus, LocalDateTime leaveDateTime) {
        this.memberStatus = memberStatus;
        this.leaveDateTime = leaveDateTime;
    }

    public String getAuthorityKey() {
        return this.authority.getKey();
    }

    public Member editNicknameAndProfileImage(String nickname, String profileImage) {
        if (StringUtils.hasText(nickname)) {
            this.nickname = nickname;
        }
        if (StringUtils.hasText(profileImage)) {
            this.profileImage = profileImage;
        }
        return this;
    }
}
