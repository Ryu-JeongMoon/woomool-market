package com.woomoolmarket.security.jwt;

public class TokenConstant {

    public static final String REDIS_KEY_PREFIX = "logout:";
    public static final String BEARER_TYPE = "Bearer";
    public static final String AUTHORITIES_KEY = "auth";
    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;
}
