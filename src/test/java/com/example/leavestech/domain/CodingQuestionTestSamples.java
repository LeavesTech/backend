package com.example.leavestech.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CodingQuestionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CodingQuestion getCodingQuestionSample1() {
        return new CodingQuestion().id(1L).title("title1").description("description1").maxScore(1).starterCode("starterCode1");
    }

    public static CodingQuestion getCodingQuestionSample2() {
        return new CodingQuestion().id(2L).title("title2").description("description2").maxScore(2).starterCode("starterCode2");
    }

    public static CodingQuestion getCodingQuestionRandomSampleGenerator() {
        return new CodingQuestion()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .maxScore(intCount.incrementAndGet())
            .starterCode(UUID.randomUUID().toString());
    }
}
