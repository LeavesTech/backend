package com.example.leavestech.service.mapper;

import com.example.leavestech.domain.AuthUser;
import com.example.leavestech.domain.Teacher;
import com.example.leavestech.service.dto.AuthUserDTO;
import com.example.leavestech.service.dto.TeacherDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Teacher} and its DTO {@link TeacherDTO}.
 */
@Mapper(componentModel = "spring")
public interface TeacherMapper extends EntityMapper<TeacherDTO, Teacher> {
    @Mapping(target = "user", source = "user", qualifiedByName = "authUserId")
    TeacherDTO toDto(Teacher s);

    @Named("authUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AuthUserDTO toDtoAuthUserId(AuthUser authUser);
}
