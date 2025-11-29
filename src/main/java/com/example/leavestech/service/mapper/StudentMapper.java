package com.example.leavestech.service.mapper;

import com.example.leavestech.domain.Department;
import com.example.leavestech.domain.Student;
import com.example.leavestech.domain.User;
import com.example.leavestech.service.dto.DepartmentDTO;
import com.example.leavestech.service.dto.StudentDTO;
import com.example.leavestech.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Student} and its DTO {@link StudentDTO}.
 */
@Mapper(componentModel = "spring")
public interface StudentMapper extends EntityMapper<StudentDTO, Student> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "department", source = "department", qualifiedByName = "departmentId")
    StudentDTO toDto(Student s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("departmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DepartmentDTO toDtoDepartmentId(Department department);
}
