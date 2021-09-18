package com.woomoolmarket.common.util;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Log4j2
class LocalDateTimeUtilTest {

    @Test
    @DisplayName("compareMonth 달 비교")
    void timeUtilTest() {
        LocalDateTimeUtil localDateTimeUtil = new LocalDateTimeUtil();

        int intervalCompareMonth = localDateTimeUtil.compareMonth(
            LocalDateTime.of(2021, 10, 6, 14, 55, 43),
            LocalDateTime.of(2021, 3, 5, 14, 34, 42));

        int intervalCompareTo =
            LocalDateTime.of(2021, 5, 6, 14, 55, 43).compareTo(
                LocalDateTime.of(2021, 3, 5, 14, 34, 42));

        assertThat(intervalCompareMonth).isEqualTo(7);
        assertThat(intervalCompareTo).isEqualTo(2);
    }

    @Test
    @DisplayName("잘못된 입력 값은 오류 터트린다")
    void timeUtilFailTest() {
        LocalDateTimeUtil localDateTimeUtil = new LocalDateTimeUtil();

        assertThrows(DateTimeException.class, () -> localDateTimeUtil.compareMonth(
            LocalDateTime.of(2021, 0, 6, 14, 55, 43),
            LocalDateTime.of(2021, 16, 5, 14, 34, 42)));
    }
}