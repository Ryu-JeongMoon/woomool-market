package com.woomoolmarket.common.util;

import java.time.LocalDateTime;

public class LocalDateTimeUtil {

    public static boolean compareMonthWithNow(LocalDateTime comparison, int months) {
        return LocalDateTime.now().isAfter(comparison.plusMonths(months));
    }

    public static boolean compareDaysWithNow(LocalDateTime comparison, int days) {
        return LocalDateTime.now().isAfter(comparison.plusDays(days));
    }
}
