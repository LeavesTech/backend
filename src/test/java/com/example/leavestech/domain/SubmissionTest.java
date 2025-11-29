package com.example.leavestech.domain;

import static com.example.leavestech.domain.CodingQuestionTestSamples.*;
import static com.example.leavestech.domain.ExamTestSamples.*;
import static com.example.leavestech.domain.StudentTestSamples.*;
import static com.example.leavestech.domain.SubmissionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.leavestech.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubmissionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Submission.class);
        Submission submission1 = getSubmissionSample1();
        Submission submission2 = new Submission();
        assertThat(submission1).isNotEqualTo(submission2);

        submission2.setId(submission1.getId());
        assertThat(submission1).isEqualTo(submission2);

        submission2 = getSubmissionSample2();
        assertThat(submission1).isNotEqualTo(submission2);
    }

    @Test
    void studentTest() {
        Submission submission = getSubmissionRandomSampleGenerator();
        Student studentBack = getStudentRandomSampleGenerator();

        submission.setStudent(studentBack);
        assertThat(submission.getStudent()).isEqualTo(studentBack);

        submission.student(null);
        assertThat(submission.getStudent()).isNull();
    }

    @Test
    void questionTest() {
        Submission submission = getSubmissionRandomSampleGenerator();
        CodingQuestion codingQuestionBack = getCodingQuestionRandomSampleGenerator();

        submission.setQuestion(codingQuestionBack);
        assertThat(submission.getQuestion()).isEqualTo(codingQuestionBack);

        submission.question(null);
        assertThat(submission.getQuestion()).isNull();
    }

    @Test
    void examTest() {
        Submission submission = getSubmissionRandomSampleGenerator();
        Exam examBack = getExamRandomSampleGenerator();

        submission.setExam(examBack);
        assertThat(submission.getExam()).isEqualTo(examBack);

        submission.exam(null);
        assertThat(submission.getExam()).isNull();
    }
}
