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
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
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
    private Status status = Status.ACTIVE;

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
        this.provider = provider != null ? provider : AuthProvider.LOCAL;
        this.authority = authority != null ? authority : Authority.ROLE_USER;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void assignAuthority(Authority authority) {
        this.authority = authority;
    }

    public void leave() {
        changeStatusAndLeaveDateTime(Status.INACTIVE, LocalDateTime.now());
    }

    public void restore() {
        changeStatusAndLeaveDateTime(Status.ACTIVE, null);
    }

    public void changeStatusAndLeaveDateTime(Status memberStatus, LocalDateTime leaveDateTime) {
        this.status = memberStatus;
        this.leaveDateTime = leaveDateTime;
    }

    public String getAuthorityKey() {
        return this.authority.getKey();
    }

    // updateFromDto 를 쓰고 있는 상황에서 이 메서드가 있을 필요가 있는가
    // core -> service 의존 관계 생기지 않으려면 필요할 듯
    public Member editNicknameAndProfileImage(String nickname, String profileImage) {
        this.nickname = StringUtils.hasText(nickname) ? nickname : this.nickname;
        this.profileImage = StringUtils.hasText(profileImage) ? profileImage : this.profileImage;
        return this;
    }
}
