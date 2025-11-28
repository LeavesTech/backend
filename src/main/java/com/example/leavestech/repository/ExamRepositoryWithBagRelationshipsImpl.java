package com.example.leavestech.repository;

import com.example.leavestech.domain.Exam;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ExamRepositoryWithBagRelationshipsImpl implements ExamRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String EXAMS_PARAMETER = "exams";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Exam> fetchBagRelationships(Optional<Exam> exam) {
        return exam.map(this::fetchQuestions);
    }

    @Override
    public Page<Exam> fetchBagRelationships(Page<Exam> exams) {
        return new PageImpl<>(fetchBagRelationships(exams.getContent()), exams.getPageable(), exams.getTotalElements());
    }

    @Override
    public List<Exam> fetchBagRelationships(List<Exam> exams) {
        return Optional.of(exams).map(this::fetchQuestions).orElse(Collections.emptyList());
    }

    Exam fetchQuestions(Exam result) {
        return entityManager
            .createQuery("select exam from Exam exam left join fetch exam.questions where exam.id = :id", Exam.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Exam> fetchQuestions(List<Exam> exams) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, exams.size()).forEach(index -> order.put(exams.get(index).getId(), index));
        List<Exam> result = entityManager
            .createQuery("select exam from Exam exam left join fetch exam.questions where exam in :exams", Exam.class)
            .setParameter(EXAMS_PARAMETER, exams)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
