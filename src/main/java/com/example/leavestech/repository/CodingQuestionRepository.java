package com.example.leavestech.repository;

import com.example.leavestech.domain.CodingQuestion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CodingQuestion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CodingQuestionRepository extends JpaRepository<CodingQuestion, Long> {}
