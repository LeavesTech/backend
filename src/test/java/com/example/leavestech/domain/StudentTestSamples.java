package com.example.leavestech.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StudentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Student getStudentSample1() {
        return new Student().id(1L).studentNumber("studentNumber1");
    }

    public static Student getStudentSample2() {
        return new Student().id(2L).studentNumber("studentNumber2");
    }

    public static Student getStudentRandomSampleGenerator() {
        return new Student().id(longCount.incrementAndGet()).studentNumber(UUID.randomUUID().toString());
    }
}
