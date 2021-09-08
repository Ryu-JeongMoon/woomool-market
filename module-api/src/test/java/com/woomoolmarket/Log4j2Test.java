package com.woomoolmarket;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

@Log4j2
class Log4j2Test {

    @Test
    void logTest() {
        log.info("log -> {}", log.getClass());
    }
}
