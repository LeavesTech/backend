package com.example.leavestech.service.mapper;

import com.example.leavestech.domain.CodingQuestion;
import com.example.leavestech.domain.Exam;
import com.example.leavestech.domain.Student;
import com.example.leavestech.domain.Submission;
import com.example.leavestech.service.dto.CodingQuestionDTO;
import com.example.leavestech.service.dto.ExamDTO;
import com.example.leavestech.service.dto.StudentDTO;
import com.example.leavestech.service.dto.SubmissionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Submission} and its DTO {@link SubmissionDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubmissionMapper extends EntityMapper<SubmissionDTO, Submission> {
    @Mapping(target = "student", source = "student", qualifiedByName = "studentId")
    @Mapping(target = "question", source = "question", qualifiedByName = "codingQuestionId")
    @Mapping(target = "exam", source = "exam", qualifiedByName = "examId")
    SubmissionDTO toDto(Submission s);

    @Named("studentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StudentDTO toDtoStudentId(Student student);

    @Named("codingQuestionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CodingQuestionDTO toDtoCodingQuestionId(CodingQuestion codingQuestion);

    @Named("examId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExamDTO toDtoExamId(Exam exam);
}
