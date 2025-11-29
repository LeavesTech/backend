package com.example.leavestech.repository;

import com.example.leavestech.domain.CourseEnrollment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CourseEnrollment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollment, Long> {}
