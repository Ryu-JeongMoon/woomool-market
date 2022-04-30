package com.woomoolmarket.util;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeUtils {

  public static boolean compareMonthWithNow(LocalDateTime comparison, int months) {
    return LocalDateTime.now().isAfter(comparison.plusMonths(months));
  }

  public static boolean compareDaysWithNow(LocalDateTime comparison, int days) {
    return LocalDateTime.now().isAfter(comparison.plusDays(days));
  }

  public static boolean isBeforeThanNow(Date toBeCompareDate) {
    return toBeCompareDate.before(new Date());
  }

  public static boolean isAfterThanNow(Date toBeCompareDate) {
    return toBeCompareDate.after(new Date());
  }
}
