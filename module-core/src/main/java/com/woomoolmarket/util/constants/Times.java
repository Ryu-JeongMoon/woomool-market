package com.woomoolmarket.util.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Getter
@RequiredArgsConstructor
public enum Times {

  ID_TOKEN_EXPIRATION_SECONDS(60 * 30),
  ACCESS_TOKEN_EXPIRATION_SECONDS(60 * 30),
  REFRESH_TOKEN_EXPIRATION_SECONDS(60 * 60 * 24 * 365),
  COOKIE_EXPIRATION_SECONDS(60 * 60 * 24 * 365);

  private final int value;

  public static Times caseInsensitiveValueOf(String name) {
    return valueOf(StringUtils.upperCase(name));
  }

  public static long toMillis(Times times) {
    return times.getValue() * 1000L;
  }
}
