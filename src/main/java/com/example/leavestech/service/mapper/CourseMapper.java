package com.example.leavestech.service.mapper;

import com.example.leavestech.domain.Course;
import com.example.leavestech.domain.Department;
import com.example.leavestech.domain.Teacher;
import com.example.leavestech.service.dto.CourseDTO;
import com.example.leavestech.service.dto.DepartmentDTO;
import com.example.leavestech.service.dto.TeacherDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Course} and its DTO {@link CourseDTO}.
 */
@Mapper(componentModel = "spring")
public interface CourseMapper extends EntityMapper<CourseDTO, Course> {
    @Mapping(target = "owner", source = "owner", qualifiedByName = "teacherId")
    @Mapping(target = "department", source = "department", qualifiedByName = "departmentId")
    CourseDTO toDto(Course s);

    @Named("teacherId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TeacherDTO toDtoTeacherId(Teacher teacher);

    @Named("departmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DepartmentDTO toDtoDepartmentId(Department department);
}
