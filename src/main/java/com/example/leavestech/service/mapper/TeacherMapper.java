package com.example.leavestech.service.mapper;

import com.example.leavestech.domain.Teacher;
import com.example.leavestech.domain.User;
import com.example.leavestech.service.dto.TeacherDTO;
import com.example.leavestech.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Teacher} and its DTO {@link TeacherDTO}.
 */
@Mapper(componentModel = "spring")
public interface TeacherMapper extends EntityMapper<TeacherDTO, Teacher> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    TeacherDTO toDto(Teacher s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
