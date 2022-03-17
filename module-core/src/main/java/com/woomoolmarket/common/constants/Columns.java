package com.woomoolmarket.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Columns {

  public static class Member {

    public static final String EMAIL = "email";
    public static final String UNIQUE_EMAIL = "unique_email";
    public static final String MEMBER_ID = "member_id";
    public static final String PROFILE_IMAGE = "profile_image";
    public static final String AUTH_PROVIDER = "auth_provider";
  }
}
