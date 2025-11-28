package com.example.leavestech.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CourseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Course getCourseSample1() {
        return new Course().id(1L).code("code1").name("name1").term("term1");
    }

    public static Course getCourseSample2() {
        return new Course().id(2L).code("code2").name("name2").term("term2");
    }

    public static Course getCourseRandomSampleGenerator() {
        return new Course()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .term(UUID.randomUUID().toString());
    }
}
