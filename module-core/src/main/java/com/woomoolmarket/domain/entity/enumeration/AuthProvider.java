package com.woomoolmarket.domain.entity.enumeration;

import org.apache.commons.lang3.StringUtils;

public enum AuthProvider {
  LOCAL,
  GOOGLE,
  GITHUB,
  FACEBOOK,
  KAKAO,
  NAVER;

  public static AuthProvider valueOfCaseInsensitively(String name) {
    return valueOf(StringUtils.upperCase(name));
  }
}