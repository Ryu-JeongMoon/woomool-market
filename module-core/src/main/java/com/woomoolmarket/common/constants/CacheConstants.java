package com.woomoolmarket.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CacheConstants {

    public static final String LOGIN_KEY_PREFIX = "auth:login#";
    public static final String LOGOUT_KEY_PREFIX = "auth:logout#";
    public static final String LOGIN_FAILED_KEY_PREFIX = "auth:login-failed#";

    public static final String BOARD_HIT_COUNT = "BoardCountService:getHit#";
}
