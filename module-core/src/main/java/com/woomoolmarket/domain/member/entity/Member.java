package com.woomoolmarket.domain.member.entity;

import com.woomoolmarket.common.auditing.BaseEntity;
import com.woomoolmarket.common.enumeration.Status;
import java.io.Serializable;
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
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "email"}, callSuper = false)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String email;

    private String nickname;

    private String password;

    private String profileImage;

    private String phone;

    private String license;

    private LocalDateTime leaveDate;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Enumerated(EnumType.STRING)
    private Authority authority = Authority.ROLE_USER;

    @Enumerated(EnumType.STRING)
    private Status memberStatus = Status.ACTIVE;

    @Builder
    public Member(String userId, String email, String nickname, String password, String profileImage,
        String phone, String license, Address address, AuthProvider provider, Authority authority) {
        this.userId = userId;
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

    public void leave(Status memberStatus, LocalDateTime leaveDate) {
        this.memberStatus = memberStatus;
        this.leaveDate = leaveDate;
    }

    public void editProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void editNickname(String nickname) {
        this.nickname = nickname;
    }

    public Member editMemberInfo(Member newMemberInfo) {
        this.password = newMemberInfo.getPassword();
        this.profileImage = newMemberInfo.getProfileImage();
        this.phone = newMemberInfo.getPhone();
        this.license = newMemberInfo.getLicense();
        this.address = newMemberInfo.getAddress();

        return this;
    }
}
