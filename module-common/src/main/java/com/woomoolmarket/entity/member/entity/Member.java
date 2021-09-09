package com.woomoolmarket.entity.member.entity;

import static javax.persistence.FetchType.LAZY;

import com.woomoolmarket.common.BaseEntity;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = {"member_id", "email"})
public class Member extends BaseEntity implements UserDetails, Serializable {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String email;

    private String nickname;

    private String password;

    private String age;

    private String profileImage;

    private String phone;

    private String license;

    private LocalDateTime leaveDate;

    @Embedded
    private Address address;

    //@Builder.Default
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = LAZY)
    private List<Role> roles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Social social;

    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    @Builder
    public Member(String userId, String email, String nickname, String password, String age, String profileImage,
        String phone, String license, Address address, List<Role> roles, Social social, MemberStatus memberStatus,
        LocalDateTime leaveDate) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.age = age;
        this.profileImage = profileImage;
        this.phone = phone;
        this.license = license;
        this.address = address;
        this.roles = roles;
        this.social = social;
        this.memberStatus = memberStatus;
        this.leaveDate = leaveDate;
    }

    /**
     * TODO Enum -> String 으로 바꾸긴 했는데 더 좋은 방법이 있으려나?
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
            .map(role -> new SimpleGrantedAuthority(role.toString()))
            .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void changeStatus(MemberStatus memberStatus, LocalDateTime leaveDate) {
        this.memberStatus = memberStatus;
        this.leaveDate = leaveDate;
    }

    public Member changeMember(Member newMember) {
        this.password = newMember.getPassword();
        this.age = newMember.getAge();
        this.profileImage = newMember.getProfileImage();
        this.phone = newMember.getPhone();
        this.license = newMember.getLicense();
        this.address = newMember.getAddress();

        return this;
    }
}
