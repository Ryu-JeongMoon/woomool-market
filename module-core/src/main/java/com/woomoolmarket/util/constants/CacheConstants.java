package com.woomoolmarket.util.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CacheConstants {

  public static final int MAXIMAL_NUMBER_OF_WRONG_PASSWORD = 5;

  public static final String LOGOUT_KEY_PREFIX = "auth:logout#";
  public static final String LOGIN_FAILED_KEY_PREFIX = "auth:login-failed#";
  public static final String LOGIN_ACCESS_TOKEN_PREFIX = "auth:login:access-token#";
  public static final String LOGIN_REFRESH_TOKEN_PREFIX = "auth:login:refresh-token#";

  public static final String BOARD_HIT_COUNT = "BoardCountService:getHit#";
  public static final String BOARDS = "BoardService:boards";
  public static final String BOARDS_FOR_ADMIN = "BoardService:boardsForAdmin";
}
