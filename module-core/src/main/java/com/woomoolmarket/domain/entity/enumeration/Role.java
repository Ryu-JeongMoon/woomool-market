package com.woomoolmarket.domain.entity.enumeration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Role implements GrantedAuthority {

  USER("ROLE_USER", 0),
  SELLER("ROLE_SELLER", 1),
  MANAGER("ROLE_MANAGER", 2),
  ADMIN("ROLE_ADMIN", 3);

  private final String authority;
  private final int level;

  public static Role valueOfCaseInsensitively(String name) {
    return valueOf(StringUtils.upperCase(name));
  }

  public boolean isSuperiorThan(Role role) {
    if (role == null)
      return true;

    return this.level >= role.level;
  }

  public boolean isInferiorThan(Role role) {
    if (role == null)
      return false;

    return this.level < role.level;
  }

  @Override
  public String getAuthority() {
    return authority;
  }
}
