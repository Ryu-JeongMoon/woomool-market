package com.woomoolmarket.config;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Log4j2
class PasswordEncoderTest {

    PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();

    @Test
    @DisplayName("인코딩 마다 다른 비밀번호 반환")
    void passwordTest() {
        String password = "1234";

        String encodedPassword = passwordEncoder.encode(password);
        String encodedPassword2 = passwordEncoder.encode(password);

        assertNotEquals(password, encodedPassword);
        assertNotEquals(encodedPassword, encodedPassword2);
        assertTrue(passwordEncoder.matches(password, encodedPassword));
    }

}