package com.example.leavestech.service.mapper;

import static com.example.leavestech.domain.AuthUserAsserts.*;
import static com.example.leavestech.domain.AuthUserTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuthUserMapperTest {

    private AuthUserMapper authUserMapper;

    @BeforeEach
    void setUp() {
        authUserMapper = new AuthUserMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAuthUserSample1();
        var actual = authUserMapper.toEntity(authUserMapper.toDto(expected));
        assertAuthUserAllPropertiesEquals(expected, actual);
    }
}
