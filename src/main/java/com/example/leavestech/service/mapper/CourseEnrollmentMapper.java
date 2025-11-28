package com.example.leavestech.service.mapper;

import com.example.leavestech.domain.Course;
import com.example.leavestech.domain.CourseEnrollment;
import com.example.leavestech.domain.Student;
import com.example.leavestech.service.dto.CourseDTO;
import com.example.leavestech.service.dto.CourseEnrollmentDTO;
import com.example.leavestech.service.dto.StudentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CourseEnrollment} and its DTO {@link CourseEnrollmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface CourseEnrollmentMapper extends EntityMapper<CourseEnrollmentDTO, CourseEnrollment> {
    @Mapping(target = "course", source = "course", qualifiedByName = "courseId")
    @Mapping(target = "student", source = "student", qualifiedByName = "studentId")
    CourseEnrollmentDTO toDto(CourseEnrollment s);

    @Named("courseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CourseDTO toDtoCourseId(Course course);

    @Named("studentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StudentDTO toDtoStudentId(Student student);
}
