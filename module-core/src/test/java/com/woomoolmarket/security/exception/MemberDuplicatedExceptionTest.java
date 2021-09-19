package com.woomoolmarket.security.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MemberDuplicatedExceptionTest {

    @Test
    void duplicateException() {
        Assertions.assertThrows(MemberDuplicatedException.class, () -> {
            throw new MemberDuplicatedException("panda");
        });
    }
}