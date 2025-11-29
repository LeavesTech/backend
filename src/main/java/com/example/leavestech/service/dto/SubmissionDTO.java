package com.example.leavestech.service.dto;

import com.example.leavestech.domain.enumeration.SubmissionStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.example.leavestech.domain.Submission} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubmissionDTO implements Serializable {

    private Long id;

    @NotNull
    private String submittedCode;

    private Double score;

    @NotNull
    private SubmissionStatus status;

    @NotNull
    private Instant submissionDate;

    @NotNull
    private StudentDTO student;

    @NotNull
    private CodingQuestionDTO question;

    @NotNull
    private ExamDTO exam;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubmittedCode() {
        return submittedCode;
    }

    public void setSubmittedCode(String submittedCode) {
        this.submittedCode = submittedCode;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public SubmissionStatus getStatus() {
        return status;
    }

    public void setStatus(SubmissionStatus status) {
        this.status = status;
    }

    public Instant getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Instant submissionDate) {
        this.submissionDate = submissionDate;
    }

    public StudentDTO getStudent() {
        return student;
    }

    public void setStudent(StudentDTO student) {
        this.student = student;
    }

    public CodingQuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(CodingQuestionDTO question) {
        this.question = question;
    }

    public ExamDTO getExam() {
        return exam;
    }

    public void setExam(ExamDTO exam) {
        this.exam = exam;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubmissionDTO)) {
            return false;
        }

        SubmissionDTO submissionDTO = (SubmissionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, submissionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubmissionDTO{" +
            "id=" + getId() +
            ", submittedCode='" + getSubmittedCode() + "'" +
            ", score=" + getScore() +
            ", status='" + getStatus() + "'" +
            ", submissionDate='" + getSubmissionDate() + "'" +
            ", student=" + getStudent() +
            ", question=" + getQuestion() +
            ", exam=" + getExam() +
            "}";
    }
}
