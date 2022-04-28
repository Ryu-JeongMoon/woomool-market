package com.woomoolmarket.common.constants;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.util.constants.CacheConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CacheConstantsTest {

  @Test
  @DisplayName("올바른 Cache 상수 값 반환")
  void constantsTest() {
    assertThat(CacheConstants.BOARDS).isEqualTo("BoardService:boards");
    assertThat(CacheConstants.LOGOUT_KEY_PREFIX).isEqualTo("auth:logout#");
    assertThat(CacheConstants.BOARD_HIT_COUNT).isEqualTo("BoardCountService:getHit#");
    assertThat(CacheConstants.LOGIN_FAILED_KEY_PREFIX).isEqualTo("auth:login-failed#");
    assertThat(CacheConstants.BOARDS_FOR_ADMIN).isEqualTo("BoardService:boardsForAdmin");
    assertThat(CacheConstants.LOGIN_ACCESS_TOKEN_PREFIX).isEqualTo("auth:login:access-token#");
    assertThat(CacheConstants.LOGIN_REFRESH_TOKEN_PREFIX).isEqualTo("auth:login:refresh-token#");
  }
}