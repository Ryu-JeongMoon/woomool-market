package com.woomoolmarket.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Log4j2
class LocalDateTimeUtilsTest {

    @Test
    @DisplayName("LocalDateTime 달 비교")
    void compareToLocalDateTimeTest() {
        int intervalCompareTo =
            LocalDateTime.of(2021, 5, 6, 14, 55, 43)
                .compareTo(LocalDateTime.of(2021, 3, 5, 14, 34, 42));

        assertEquals(intervalCompareTo, 2);
    }

    @Test
    @DisplayName("compareMonth 달 비교")
    void compareMonthTest() {
        LocalDateTime someDay = LocalDateTime.of(2021, 3, 5, 14, 34, 42);
        boolean canBeCanceled = LocalDateTimeUtils.compareMonthWithNow(someDay, 5);

        assertTrue(canBeCanceled);
    }

    @Test
    @DisplayName("LocalDate 일 비교")
    void compareDayTest() {
        LocalDate date = LocalDate.of(2021, 9, 15);
        LocalDate now = LocalDate.now();
        assertTrue(now.isAfter(date.plusDays(3)));
    }

    @Test
    @DisplayName("compareDays 일 비교")
    void compareDaysWithNowTest() {
        LocalDateTime date = LocalDateTime.of(2021, 9, 21, 2, 2, 3);
        assertTrue(LocalDateTimeUtils.compareDaysWithNow(date, 3));
    }

    @Test
    @DisplayName("잘못된 입력 값은 오류 터트린다")
    void timeUtilFailTest() {
        assertThrows(DateTimeException.class, () -> LocalDateTimeUtils.compareMonthWithNow(
            LocalDateTime.of(2021, 16, 5, 14, 34, 42), 5));
    }
}