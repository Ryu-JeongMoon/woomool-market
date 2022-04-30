package com.woomoolmarket.util.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ColumnConstants {

  public static class Length {

    public static final int DEFAULT_MAX = 255;
    public static final int DEFAULT_STRING = 20;

    public static final int ID = 36;
    public static final int EMAIL_MIN = 7;
    public static final int EMAIL_MAX = 320;
    public static final int NICKNAME_MIN = 1;
    public static final int NICKNAME_MAX = 20;
    public static final int PASSWORD_MIN = 4;
    public static final int ENCODED_PASSWORD = 96;
    public static final int REFRESH_TOKEN_MAX = 266;

    public static final int SEARCH = 20;
  }

  public static class Name {

    public static final String CREATED_AT = "created_at";
    public static final String CREATED_BY = "created_by";
    public static final String MODIFIED_AT = "modified_at";
    public static final String MODIFIED_BY = "modified_by";

    public static final String MEMBER_ID = "member_id";
    public static final String EMAIL = "email";
    public static final String LEFT_AT = "left_at";
    public static final String NICKNAME = "nickname";
    public static final String PASSWORD = "password";
    public static final String PROFILE_IMAGE = "profile_image";
    public static final String INITIAL_AUTH_PROVIDER = "initial_auth_provider";
    public static final String LATEST_AUTH_PROVIDER = "latest_auth_provider";
    public static final String UNIQUE_EMAIL = "uk_member_email";

    public static final String POST_ID = "post_id";
    public static final String TITLE = "title";
    public static final String INDEX_TITLE = "ix_title";
    public static final String UNIQUE_TITLE = "uk_post_title";
    public static final String OPENED_AT = "opened_at";
    public static final String CLOSED_AT = "closed_at";

    public static final String TOKEN_ID = "token_id";
    public static final String TOKEN_VALUE = "token_value";
    public static final String AUTH_PROVIDER = "auth_provider";
    public static final String EXPIRED_AT = "expired_at";
  }
}
