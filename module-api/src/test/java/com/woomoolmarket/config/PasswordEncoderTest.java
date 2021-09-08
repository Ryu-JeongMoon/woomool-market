package com.woomoolmarket.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordEncoderTest {

    PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();

    @Test
    void passwordTest() {
        String name = "panda";
        String password = "1234";

        String encodedPassword = passwordEncoder.encode(password);
        String encodedPassword2 = passwordEncoder.encode(password);

        System.out.println("password = " + password);
        System.out.println("encodedPassword = " + encodedPassword);
        System.out.println("encodedPassword2 = " + encodedPassword2);

        assertNotEquals(password, encodedPassword);
        assertTrue(passwordEncoder.matches(password, encodedPassword));
    }

}