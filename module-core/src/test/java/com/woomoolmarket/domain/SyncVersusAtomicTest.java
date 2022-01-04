package com.woomoolmarket.domain;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

class SyncVersusAtomicTest {

  private int count1;
  private final AtomicInteger count2 = new AtomicInteger(0);
  private final StopWatch stopWatch = new StopWatch("panda");

  @Test
  @DisplayName("AtomicInteger 가 synchronized 연산 속도보다 빠르다")
  void syncTest() {
    stopWatch.start("sync");
    for (int i = 0; i < 100; i++) {
      System.out.println(sync());
    }
    stopWatch.stop();

    stopWatch.start("atomic");
    for (int i = 0; i < 100; i++) {
      System.out.println(atomic());
    }
    stopWatch.stop();

    System.out.println(stopWatch.prettyPrint());
  }

  private synchronized int sync() {
    return ++count1;
  }

  private int atomic() {
    return count2.incrementAndGet();
  }
}
