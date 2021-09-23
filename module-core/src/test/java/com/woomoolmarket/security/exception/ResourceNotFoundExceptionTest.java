package com.woomoolmarket.security.exception;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.woomoolmarket.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ResourceNotFoundExceptionTest {

    @Test
    @DisplayName("ResourceNotFoundException가 터진다")
    void customExceptionTest() {
        assertThrows(ResourceNotFoundException.class, () -> {
            throw new ResourceNotFoundException("panda", "bear", 12345);
        });
    }
}