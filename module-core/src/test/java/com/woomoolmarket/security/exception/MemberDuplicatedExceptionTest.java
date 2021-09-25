package com.woomoolmarket.security.exception;

import com.woomoolmarket.exception.member.UsernameDuplicatedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MemberDuplicatedExceptionTest {

    @Test
    void duplicateException() {
        Assertions.assertThrows(UsernameDuplicatedException.class, () -> {
            throw new UsernameDuplicatedException("panda");
        });
    }
}