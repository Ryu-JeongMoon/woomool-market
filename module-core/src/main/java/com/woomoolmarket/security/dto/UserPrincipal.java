package com.woomoolmarket.security.dto;

import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.entity.enumeration.Role;
import com.woomoolmarket.domain.entity.enumeration.Status;
import java.util.Collection;
import java.util.Collections;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@ToString
@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {

  private final Long id;
  private final String email;
  private final String password;
  private final Status status;
  private final Collection<Role> authorities;

  public static UserPrincipal from(Member member) {
    return UserPrincipal.of(member.getId(), member.getEmail(), member.getPassword(), Collections.singletonList(member.getRole()));
  }

  public static UserPrincipal of(Long id, String email, String password, Collection<Role> authorities) {
    return new UserPrincipal(id, email, password, Status.ACTIVE, authorities);
  }

  public Authentication toAuthentication() {
    return new UsernamePasswordAuthenticationToken(this, this.password, this.authorities);
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
  public Collection<Role> getAuthorities() {
    return authorities;
  }

  @Override
  public boolean isEnabled() {
    return Status.ACTIVE == status;
  }

  @Override
  public boolean isAccountNonExpired() {
    return isEnabled();
  }

  @Override
  public boolean isAccountNonLocked() {
    return isEnabled();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return isEnabled();
  }
}