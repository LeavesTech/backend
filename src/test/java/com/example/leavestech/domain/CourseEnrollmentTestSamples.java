package com.example.leavestech.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class CourseEnrollmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CourseEnrollment getCourseEnrollmentSample1() {
        return new CourseEnrollment().id(1L);
    }

    public static CourseEnrollment getCourseEnrollmentSample2() {
        return new CourseEnrollment().id(2L);
    }

    public static CourseEnrollment getCourseEnrollmentRandomSampleGenerator() {
        return new CourseEnrollment().id(longCount.incrementAndGet());
    }
}
