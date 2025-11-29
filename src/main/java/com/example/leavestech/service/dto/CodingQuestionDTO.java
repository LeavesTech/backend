package com.example.leavestech.service.dto;

import com.example.leavestech.domain.enumeration.ProgrammingLanguage;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.example.leavestech.domain.CodingQuestion} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CodingQuestionDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private Integer maxScore;

    @NotNull
    private String starterCode;

    @NotNull
    private ProgrammingLanguage language;

    private Set<ExamDTO> exams = new HashSet<>();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    public String getStarterCode() {
        return starterCode;
    }

    public void setStarterCode(String starterCode) {
        this.starterCode = starterCode;
    }

    public ProgrammingLanguage getLanguage() {
        return language;
    }

    public void setLanguage(ProgrammingLanguage language) {
        this.language = language;
    }

    public Set<ExamDTO> getExams() {
        return exams;
    }

    public void setExams(Set<ExamDTO> exams) {
        this.exams = exams;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CodingQuestionDTO)) {
            return false;
        }

        CodingQuestionDTO codingQuestionDTO = (CodingQuestionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, codingQuestionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CodingQuestionDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", maxScore=" + getMaxScore() +
            ", starterCode='" + getStarterCode() + "'" +
            ", language='" + getLanguage() + "'" +
            ", exams=" + getExams() +
            "}";
    }
}
