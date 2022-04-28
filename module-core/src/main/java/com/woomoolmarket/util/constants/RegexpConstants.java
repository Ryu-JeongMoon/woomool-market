package com.woomoolmarket.util.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegexpConstants {

  public static final String EMAIL = "(?i)^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";

  public static final String LETTER_ONLY = "^[a-zA-Z가-힣]*$";

  public static final String NUMBER_ONLY = "^[\\d]*$";

  public static final String KOREAN_ONLY = "^[가-힣]*$";

  public static final String ENGLISH_ONLY = "^[a-zA-Z]*$";

  public static final String LETTER_AND_NUMBER = "^[\\w]*$";

  public static final String SPECIAL_LETTER_INCLUDE = "[\\w!?@#$%^&*():;+-=~{}<>\\_\\[\\]\\|\\\\\\\"\\'\\,\\.\\/\\`\\₩]*";

}
