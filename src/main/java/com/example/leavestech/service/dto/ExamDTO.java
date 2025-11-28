package com.example.leavestech.service.dto;

import com.example.leavestech.domain.enumeration.ExamType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.example.leavestech.domain.Exam} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExamDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private Instant startedAt;

    @NotNull
    private Instant endAt;

    @NotNull
    private Integer durationTime;

    @NotNull
    private ExamType type;

    @NotNull
    private CourseDTO course;

    private Set<CodingQuestionDTO> questions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getEndAt() {
        return endAt;
    }

    public void setEndAt(Instant endAt) {
        this.endAt = endAt;
    }

    public Integer getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(Integer durationTime) {
        this.durationTime = durationTime;
    }

    public ExamType getType() {
        return type;
    }

    public void setType(ExamType type) {
        this.type = type;
    }

    public CourseDTO getCourse() {
        return course;
    }

    public void setCourse(CourseDTO course) {
        this.course = course;
    }

    public Set<CodingQuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<CodingQuestionDTO> questions) {
        this.questions = questions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExamDTO)) {
            return false;
        }

        ExamDTO examDTO = (ExamDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, examDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExamDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", startedAt='" + getStartedAt() + "'" +
            ", endAt='" + getEndAt() + "'" +
            ", durationTime=" + getDurationTime() +
            ", type='" + getType() + "'" +
            ", course=" + getCourse() +
            ", questions=" + getQuestions() +
            "}";
    }
}
