package com.woomoolmarket.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogConstants {

  public static final String DELETE_FAILED = "[WOOMOOL-ERROR] :: 삭제 실패 => {}";
  public static final String DELETE_COMPLETED = "[WOOMOOL-COMPLETED] :: 삭제 완료 => {}";

}
