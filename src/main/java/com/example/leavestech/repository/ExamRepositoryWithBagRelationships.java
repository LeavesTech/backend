package com.example.leavestech.repository;

import com.example.leavestech.domain.Exam;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ExamRepositoryWithBagRelationships {
    Optional<Exam> fetchBagRelationships(Optional<Exam> exam);

    List<Exam> fetchBagRelationships(List<Exam> exams);

    Page<Exam> fetchBagRelationships(Page<Exam> exams);
}
