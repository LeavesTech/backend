package com.example.leavestech.domain;

import com.example.leavestech.domain.enumeration.ProgrammingLanguage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CodingQuestion.
 */
@Entity
@Table(name = "coding_question")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CodingQuestion implements Serializable {

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
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "max_score", nullable = false)
    private Integer maxScore;

    @NotNull
    @Column(name = "starter_code", nullable = false)
    private String starterCode;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    private ProgrammingLanguage language;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "questions")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "course", "questions" }, allowSetters = true)
    private Set<Exam> exams = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CodingQuestion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public CodingQuestion title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public CodingQuestion description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaxScore() {
        return this.maxScore;
    }

    public CodingQuestion maxScore(Integer maxScore) {
        this.setMaxScore(maxScore);
        return this;
    }

    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    public String getStarterCode() {
        return this.starterCode;
    }

    public CodingQuestion starterCode(String starterCode) {
        this.setStarterCode(starterCode);
        return this;
    }

    public void setStarterCode(String starterCode) {
        this.starterCode = starterCode;
    }

    public ProgrammingLanguage getLanguage() {
        return this.language;
    }

    public CodingQuestion language(ProgrammingLanguage language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(ProgrammingLanguage language) {
        this.language = language;
    }

    public Set<Exam> getExams() {
        return this.exams;
    }

    public void setExams(Set<Exam> exams) {
        if (this.exams != null) {
            this.exams.forEach(i -> i.removeQuestions(this));
        }
        if (exams != null) {
            exams.forEach(i -> i.addQuestions(this));
        }
        this.exams = exams;
    }

    public CodingQuestion exams(Set<Exam> exams) {
        this.setExams(exams);
        return this;
    }

    public CodingQuestion addExams(Exam exam) {
        this.exams.add(exam);
        exam.getQuestions().add(this);
        return this;
    }

    public CodingQuestion removeExams(Exam exam) {
        this.exams.remove(exam);
        exam.getQuestions().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CodingQuestion)) {
            return false;
        }
        return getId() != null && getId().equals(((CodingQuestion) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CodingQuestion{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", maxScore=" + getMaxScore() +
            ", starterCode='" + getStarterCode() + "'" +
            ", language='" + getLanguage() + "'" +
            "}";
    }
}
