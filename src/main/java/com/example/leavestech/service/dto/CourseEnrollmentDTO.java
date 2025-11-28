package com.example.leavestech.service.dto;

import com.example.leavestech.domain.enumeration.EnrollmentStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.example.leavestech.domain.CourseEnrollment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CourseEnrollmentDTO implements Serializable {

    private Long id;

    @NotNull
    private EnrollmentStatus status;

    @NotNull
    private CourseDTO course;

    @NotNull
    private StudentDTO student;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    public CourseDTO getCourse() {
        return course;
    }

    public void setCourse(CourseDTO course) {
        this.course = course;
    }

    public StudentDTO getStudent() {
        return student;
    }

    public void setStudent(StudentDTO student) {
        this.student = student;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseEnrollmentDTO)) {
            return false;
        }

        CourseEnrollmentDTO courseEnrollmentDTO = (CourseEnrollmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, courseEnrollmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourseEnrollmentDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", course=" + getCourse() +
            ", student=" + getStudent() +
            "}";
    }
}
