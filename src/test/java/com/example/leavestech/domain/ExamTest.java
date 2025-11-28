package com.example.leavestech.domain;

import static com.example.leavestech.domain.CodingQuestionTestSamples.*;
import static com.example.leavestech.domain.CourseTestSamples.*;
import static com.example.leavestech.domain.ExamTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.leavestech.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ExamTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Exam.class);
        Exam exam1 = getExamSample1();
        Exam exam2 = new Exam();
        assertThat(exam1).isNotEqualTo(exam2);

        exam2.setId(exam1.getId());
        assertThat(exam1).isEqualTo(exam2);

        exam2 = getExamSample2();
        assertThat(exam1).isNotEqualTo(exam2);
    }

    @Test
    void courseTest() {
        Exam exam = getExamRandomSampleGenerator();
        Course courseBack = getCourseRandomSampleGenerator();

        exam.setCourse(courseBack);
        assertThat(exam.getCourse()).isEqualTo(courseBack);

        exam.course(null);
        assertThat(exam.getCourse()).isNull();
    }

    @Test
    void questionsTest() {
        Exam exam = getExamRandomSampleGenerator();
        CodingQuestion codingQuestionBack = getCodingQuestionRandomSampleGenerator();

        exam.addQuestions(codingQuestionBack);
        assertThat(exam.getQuestions()).containsOnly(codingQuestionBack);

        exam.removeQuestions(codingQuestionBack);
        assertThat(exam.getQuestions()).doesNotContain(codingQuestionBack);

        exam.questions(new HashSet<>(Set.of(codingQuestionBack)));
        assertThat(exam.getQuestions()).containsOnly(codingQuestionBack);

        exam.setQuestions(new HashSet<>());
        assertThat(exam.getQuestions()).doesNotContain(codingQuestionBack);
    }
}
