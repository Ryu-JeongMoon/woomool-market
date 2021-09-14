package com.woomoolmarket.domain.member.entity;

import com.woomoolmarket.common.BaseEntity;
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
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "email"}, callSuper = false)
public class Member extends BaseEntity implements Serializable {

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
    private Authority authority;

    @Enumerated(EnumType.STRING)
    private Social social;

    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    @Builder
    public Member(String userId, String email, String nickname, String password, String profileImage,
        String phone, String license, Address address, Authority authority, Social social, MemberStatus memberStatus,
        LocalDateTime leaveDate) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.profileImage = profileImage;
        this.phone = phone;
        this.license = license;
        this.address = address;
        this.authority = authority;
        this.social = social;
        this.memberStatus = memberStatus;
        this.leaveDate = leaveDate;
    }

    public void encodePassword(String password) {
        this.password = password;
    }

    public void registerAuthority(Authority authority) {
        this.authority = authority;
    }

    public void changeStatus(MemberStatus memberStatus, LocalDateTime leaveDate) {
        this.memberStatus = memberStatus;
        this.leaveDate = leaveDate;
    }

    public Member changeMemberInfo(Member newMemberInfo) {
        this.password = newMemberInfo.getPassword();
        this.profileImage = newMemberInfo.getProfileImage();
        this.phone = newMemberInfo.getPhone();
        this.license = newMemberInfo.getLicense();
        this.address = newMemberInfo.getAddress();

        return this;
    }
}
