package com.example.leavestech.domain;

import static com.example.leavestech.domain.CodingQuestionTestSamples.*;
import static com.example.leavestech.domain.ExamTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.leavestech.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CodingQuestionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CodingQuestion.class);
        CodingQuestion codingQuestion1 = getCodingQuestionSample1();
        CodingQuestion codingQuestion2 = new CodingQuestion();
        assertThat(codingQuestion1).isNotEqualTo(codingQuestion2);

        codingQuestion2.setId(codingQuestion1.getId());
        assertThat(codingQuestion1).isEqualTo(codingQuestion2);

        codingQuestion2 = getCodingQuestionSample2();
        assertThat(codingQuestion1).isNotEqualTo(codingQuestion2);
    }

    @Test
    void examsTest() {
        CodingQuestion codingQuestion = getCodingQuestionRandomSampleGenerator();
        Exam examBack = getExamRandomSampleGenerator();

        codingQuestion.addExams(examBack);
        assertThat(codingQuestion.getExams()).containsOnly(examBack);
        assertThat(examBack.getQuestions()).containsOnly(codingQuestion);

        codingQuestion.removeExams(examBack);
        assertThat(codingQuestion.getExams()).doesNotContain(examBack);
        assertThat(examBack.getQuestions()).doesNotContain(codingQuestion);

        codingQuestion.exams(new HashSet<>(Set.of(examBack)));
        assertThat(codingQuestion.getExams()).containsOnly(examBack);
        assertThat(examBack.getQuestions()).containsOnly(codingQuestion);

        codingQuestion.setExams(new HashSet<>());
        assertThat(codingQuestion.getExams()).doesNotContain(examBack);
        assertThat(examBack.getQuestions()).doesNotContain(codingQuestion);
    }
}
