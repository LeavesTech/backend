package com.example.leavestech.service.mapper;

import com.example.leavestech.domain.CodingQuestion;
import com.example.leavestech.domain.Course;
import com.example.leavestech.domain.Exam;
import com.example.leavestech.service.dto.CodingQuestionDTO;
import com.example.leavestech.service.dto.CourseDTO;
import com.example.leavestech.service.dto.ExamDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Exam} and its DTO {@link ExamDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExamMapper extends EntityMapper<ExamDTO, Exam> {
    @Mapping(target = "course", source = "course", qualifiedByName = "courseId")
    @Mapping(target = "questions", source = "questions", qualifiedByName = "codingQuestionIdSet")
    ExamDTO toDto(Exam s);

    @Mapping(target = "removeQuestions", ignore = true)
    Exam toEntity(ExamDTO examDTO);

    @Named("courseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CourseDTO toDtoCourseId(Course course);

    @Named("codingQuestionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CodingQuestionDTO toDtoCodingQuestionId(CodingQuestion codingQuestion);

    @Named("codingQuestionIdSet")
    default Set<CodingQuestionDTO> toDtoCodingQuestionIdSet(Set<CodingQuestion> codingQuestion) {
        return codingQuestion.stream().map(this::toDtoCodingQuestionId).collect(Collectors.toSet());
    }
}
