package com.example.leavestech.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SubmissionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Submission getSubmissionSample1() {
        return new Submission().id(1L).submittedCode("submittedCode1");
    }

    public static Submission getSubmissionSample2() {
        return new Submission().id(2L).submittedCode("submittedCode2");
    }

    public static Submission getSubmissionRandomSampleGenerator() {
        return new Submission().id(longCount.incrementAndGet()).submittedCode(UUID.randomUUID().toString());
    }
}
