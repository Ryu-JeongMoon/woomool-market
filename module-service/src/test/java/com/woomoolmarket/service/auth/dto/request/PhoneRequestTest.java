package com.woomoolmarket.service.auth.dto.request;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PhoneRequestTest {

    @Test
    void getPhone() {
        PhoneRequest phoneRequest = PhoneRequest.builder()
            .phone("01012345678")
            .build();
        assertThat(phoneRequest.getPhone()).isEqualTo("01012345678");
    }

    @Test
    void setPhone() {
        PhoneRequest phoneRequest = new PhoneRequest();
        phoneRequest.setPhone("01012345678");
        assertThat(phoneRequest.getPhone()).isEqualTo("01012345678");
    }
}