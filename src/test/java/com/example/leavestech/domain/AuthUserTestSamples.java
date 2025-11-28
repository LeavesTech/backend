package com.example.leavestech.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AuthUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AuthUser getAuthUserSample1() {
        return new AuthUser()
            .id(1L)
            .username("username1")
            .email("email1")
            .phoneNumber("phoneNumber1")
            .firstName("firstName1")
            .lastName("lastName1")
            .password("password1");
    }

    public static AuthUser getAuthUserSample2() {
        return new AuthUser()
            .id(2L)
            .username("username2")
            .email("email2")
            .phoneNumber("phoneNumber2")
            .firstName("firstName2")
            .lastName("lastName2")
            .password("password2");
    }

    public static AuthUser getAuthUserRandomSampleGenerator() {
        return new AuthUser()
            .id(longCount.incrementAndGet())
            .username(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .password(UUID.randomUUID().toString());
    }
}
