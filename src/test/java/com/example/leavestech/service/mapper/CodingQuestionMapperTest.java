package com.example.leavestech.service.mapper;

import static com.example.leavestech.domain.CodingQuestionAsserts.*;
import static com.example.leavestech.domain.CodingQuestionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CodingQuestionMapperTest {

    private CodingQuestionMapper codingQuestionMapper;

    @BeforeEach
    void setUp() {
        codingQuestionMapper = new CodingQuestionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCodingQuestionSample1();
        var actual = codingQuestionMapper.toEntity(codingQuestionMapper.toDto(expected));
        assertCodingQuestionAllPropertiesEquals(expected, actual);
    }
}
