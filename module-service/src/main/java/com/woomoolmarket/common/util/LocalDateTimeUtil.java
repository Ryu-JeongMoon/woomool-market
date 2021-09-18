package com.woomoolmarket.common.util;

import java.time.LocalDateTime;

public class LocalDateTimeUtil {

    public static int compareMonth(LocalDateTime original, LocalDateTime comparison) {
        return original.compareTo(comparison);
    }
}
