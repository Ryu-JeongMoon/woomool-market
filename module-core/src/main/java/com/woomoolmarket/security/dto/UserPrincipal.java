package com.woomoolmarket.security.dto;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.member.entity.Member;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RequiredArgsConstructor
public class UserPrincipal implements OAuth2User, UserDetails {

    private final String email;
    private final String password;
    private final Status status;
    private final Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public static UserPrincipal of(Member member) {
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(member.getAuthorityKey()));

        return new UserPrincipal(
            member.getEmail(),
            member.getPassword(),
            member.getStatus(),
            authorities
        );
    }

    public static UserPrincipal of(Member member, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.of(member);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getName() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public boolean isAccountNonExpired() {
        return Status.ACTIVE.equals(status);
    }

    @Override
    public boolean isAccountNonLocked() {
        return Status.ACTIVE.equals(status);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return Status.ACTIVE.equals(status);
    }

    @Override
    public boolean isEnabled() {
        return Status.ACTIVE.equals(status);
    }
}
