package com.example.leavestech.domain;

import com.example.leavestech.domain.audit.AbstractSoftDeleteAuditingEntity;
import com.example.leavestech.domain.enumeration.ExamType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

/**
 * A Exam.
 */
@Entity
@Table(name = "exam")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
@SQLDelete(sql = "UPDATE exam SET deleted = true , deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted = false")
public class Exam extends AbstractSoftDeleteAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @NotNull
    @Column(name = "end_at", nullable = false)
    private Instant endAt;

    @NotNull
    @Column(name = "duration_time", nullable = false)
    private Integer durationTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ExamType type;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "owner", "department" }, allowSetters = true)
    private Course course;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_exam__questions",
        joinColumns = @JoinColumn(name = "exam_id"),
        inverseJoinColumns = @JoinColumn(name = "questions_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "exams" }, allowSetters = true)
    private Set<CodingQuestion> questions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Exam id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Exam title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getStartedAt() {
        return this.startedAt;
    }

    public Exam startedAt(Instant startedAt) {
        this.setStartedAt(startedAt);
        return this;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getEndAt() {
        return this.endAt;
    }

    public Exam endAt(Instant endAt) {
        this.setEndAt(endAt);
        return this;
    }

    public void setEndAt(Instant endAt) {
        this.endAt = endAt;
    }

    public Integer getDurationTime() {
        return this.durationTime;
    }

    public Exam durationTime(Integer durationTime) {
        this.setDurationTime(durationTime);
        return this;
    }

    public void setDurationTime(Integer durationTime) {
        this.durationTime = durationTime;
    }

    public ExamType getType() {
        return this.type;
    }

    public Exam type(ExamType type) {
        this.setType(type);
        return this;
    }

    public void setType(ExamType type) {
        this.type = type;
    }

    public Course getCourse() {
        return this.course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Exam course(Course course) {
        this.setCourse(course);
        return this;
    }

    public Set<CodingQuestion> getQuestions() {
        return this.questions;
    }

    public void setQuestions(Set<CodingQuestion> codingQuestions) {
        this.questions = codingQuestions;
    }

    public Exam questions(Set<CodingQuestion> codingQuestions) {
        this.setQuestions(codingQuestions);
        return this;
    }

    public Exam addQuestions(CodingQuestion codingQuestion) {
        this.questions.add(codingQuestion);
        return this;
    }

    public Exam removeQuestions(CodingQuestion codingQuestion) {
        this.questions.remove(codingQuestion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Exam)) {
            return false;
        }
        return getId() != null && getId().equals(((Exam) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Exam{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", startedAt='" + getStartedAt() + "'" +
            ", endAt='" + getEndAt() + "'" +
            ", durationTime=" + getDurationTime() +
            ", type='" + getType() + "'" +
            "}";
    }
}
