package com.woomoolmarket.learning;

import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

@Slf4j
class SyncVersusAtomicTest {

  private final AtomicInteger count2 = new AtomicInteger(0);
  private final StopWatch stopWatch = new StopWatch("panda");
  private int count1;

  @Test
  @DisplayName("AtomicInteger 가 synchronized 연산 속도보다 빠르다")
  void syncTest() {
    stopWatch.start("sync");
    for (int i = 0; i < 100; i++) {
      log.info("sync = {}", sync());
    }
    stopWatch.stop();

    stopWatch.start("atomic");
    for (int i = 0; i < 100; i++) {
      log.info("atomic = {}", atomic());
    }
    stopWatch.stop();

    log.info("result = {}", stopWatch.prettyPrint());
  }

  private synchronized int sync() {
    return ++count1;
  }

  private int atomic() {
    return count2.incrementAndGet();
  }
}
