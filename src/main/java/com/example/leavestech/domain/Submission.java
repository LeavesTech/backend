package com.example.leavestech.domain;

import com.example.leavestech.domain.audit.AbstractSoftDeleteAuditingEntity;
import com.example.leavestech.domain.enumeration.SubmissionStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

/**
 * A Submission.
 */
@Entity
@Table(name = "submission")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
@SQLDelete(sql = "UPDATE submission SET deleted = true , deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted = false")
public class Submission extends AbstractSoftDeleteAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "submitted_code", nullable = false)
    private String submittedCode;

    @Column(name = "score")
    private Double score;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SubmissionStatus status;

    @NotNull
    @Column(name = "submission_date", nullable = false)
    private Instant submissionDate;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "department" }, allowSetters = true)
    private Student student;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "exams" }, allowSetters = true)
    private CodingQuestion question;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "course", "questions" }, allowSetters = true)
    private Exam exam;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Submission id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubmittedCode() {
        return this.submittedCode;
    }

    public Submission submittedCode(String submittedCode) {
        this.setSubmittedCode(submittedCode);
        return this;
    }

    public void setSubmittedCode(String submittedCode) {
        this.submittedCode = submittedCode;
    }

    public Double getScore() {
        return this.score;
    }

    public Submission score(Double score) {
        this.setScore(score);
        return this;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public SubmissionStatus getStatus() {
        return this.status;
    }

    public Submission status(SubmissionStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(SubmissionStatus status) {
        this.status = status;
    }

    public Instant getSubmissionDate() {
        return this.submissionDate;
    }

    public Submission submissionDate(Instant submissionDate) {
        this.setSubmissionDate(submissionDate);
        return this;
    }

    public void setSubmissionDate(Instant submissionDate) {
        this.submissionDate = submissionDate;
    }

    public Student getStudent() {
        return this.student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Submission student(Student student) {
        this.setStudent(student);
        return this;
    }

    public CodingQuestion getQuestion() {
        return this.question;
    }

    public void setQuestion(CodingQuestion codingQuestion) {
        this.question = codingQuestion;
    }

    public Submission question(CodingQuestion codingQuestion) {
        this.setQuestion(codingQuestion);
        return this;
    }

    public Exam getExam() {
        return this.exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public Submission exam(Exam exam) {
        this.setExam(exam);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Submission)) {
            return false;
        }
        return getId() != null && getId().equals(((Submission) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Submission{" +
            "id=" + getId() +
            ", submittedCode='" + getSubmittedCode() + "'" +
            ", score=" + getScore() +
            ", status='" + getStatus() + "'" +
            ", submissionDate='" + getSubmissionDate() + "'" +
            "}";
    }
}
