package com.woomoolmarket.domain.member.entity;

import com.woomoolmarket.common.auditing.BaseEntity;
import com.woomoolmarket.common.embeddable.Address;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.image.entity.Image;
import java.time.LocalDateTime;
import javax.annotation.Nullable;
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
import org.springframework.util.StringUtils;

@Setter
@Getter
@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "email"}, callSuper = false)
@Table(uniqueConstraints = {@UniqueConstraint(name = "unique_email", columnNames = "email")})
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 9, max = 64)
    @Column(nullable = false)
    private String email;

    @Size(min = 4, max = 24)
    @Column(nullable = false)
    private String nickname;

    @Size(min = 4, max = 255)
    @Column(nullable = false)
    private String password;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Image image;

    @Size(max = 255)
    private String profileImage;

    @Size(max = 11)
    private String phone;

    @Size(max = 10)
    private String license;

    private LocalDateTime leaveDateTime;

    @Embedded
    private Address address;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private Authority authority = Authority.ROLE_USER;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Builder
    public Member(String email, String nickname, String password, Image image, String profileImage,
        String phone, String license, Address address, AuthProvider authProvider, Authority authority) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.image = image;
        this.profileImage = profileImage;
        this.phone = phone;
        this.license = license;
        this.address = address;
        this.authProvider = authProvider != null ? authProvider : AuthProvider.LOCAL;
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

    public Member editNicknameAndProfileImage(String nickname, String profileImage) {
        this.nickname = StringUtils.hasText(nickname) ? nickname : this.nickname;
        this.profileImage = StringUtils.hasText(profileImage) ? profileImage : this.profileImage;
        return this;
    }
}
