package com.example.leavestech.service.mapper;

import com.example.leavestech.domain.AuthUser;
import com.example.leavestech.domain.Role;
import com.example.leavestech.service.dto.AuthUserDTO;
import com.example.leavestech.service.dto.RoleDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AuthUser} and its DTO {@link AuthUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface AuthUserMapper extends EntityMapper<AuthUserDTO, AuthUser> {
    @Mapping(target = "roles", source = "roles", qualifiedByName = "roleIdSet")
    AuthUserDTO toDto(AuthUser s);

    @Mapping(target = "removeRoles", ignore = true)
    AuthUser toEntity(AuthUserDTO authUserDTO);

    @Named("roleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RoleDTO toDtoRoleId(Role role);

    @Named("roleIdSet")
    default Set<RoleDTO> toDtoRoleIdSet(Set<Role> role) {
        return role.stream().map(this::toDtoRoleId).collect(Collectors.toSet());
    }
}
