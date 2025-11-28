package com.example.leavestech.service.mapper;

import static com.example.leavestech.domain.CourseEnrollmentAsserts.*;
import static com.example.leavestech.domain.CourseEnrollmentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CourseEnrollmentMapperTest {

    private CourseEnrollmentMapper courseEnrollmentMapper;

    @BeforeEach
    void setUp() {
        courseEnrollmentMapper = new CourseEnrollmentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCourseEnrollmentSample1();
        var actual = courseEnrollmentMapper.toEntity(courseEnrollmentMapper.toDto(expected));
        assertCourseEnrollmentAllPropertiesEquals(expected, actual);
    }
}
