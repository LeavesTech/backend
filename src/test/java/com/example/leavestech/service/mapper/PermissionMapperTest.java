package com.example.leavestech.service.mapper;

import static com.example.leavestech.domain.PermissionAsserts.*;
import static com.example.leavestech.domain.PermissionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PermissionMapperTest {

    private PermissionMapper permissionMapper;

    @BeforeEach
    void setUp() {
        permissionMapper = new PermissionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPermissionSample1();
        var actual = permissionMapper.toEntity(permissionMapper.toDto(expected));
        assertPermissionAllPropertiesEquals(expected, actual);
    }
}
