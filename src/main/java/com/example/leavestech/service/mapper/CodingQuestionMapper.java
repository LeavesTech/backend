package com.example.leavestech.service.mapper;

import com.example.leavestech.domain.CodingQuestion;
import com.example.leavestech.domain.Exam;
import com.example.leavestech.service.dto.CodingQuestionDTO;
import com.example.leavestech.service.dto.ExamDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CodingQuestion} and its DTO {@link CodingQuestionDTO}.
 */
@Mapper(componentModel = "spring")
public interface CodingQuestionMapper extends EntityMapper<CodingQuestionDTO, CodingQuestion> {
    @Mapping(target = "exams", source = "exams", qualifiedByName = "examIdSet")
    CodingQuestionDTO toDto(CodingQuestion s);

    @Mapping(target = "exams", ignore = true)
    @Mapping(target = "removeExams", ignore = true)
    CodingQuestion toEntity(CodingQuestionDTO codingQuestionDTO);

    @Named("examId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExamDTO toDtoExamId(Exam exam);

    @Named("examIdSet")
    default Set<ExamDTO> toDtoExamIdSet(Set<Exam> exam) {
        return exam.stream().map(this::toDtoExamId).collect(Collectors.toSet());
    }
}
