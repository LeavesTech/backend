package com.example.leavestech.service.mapper;

import com.example.leavestech.domain.Permission;
import com.example.leavestech.domain.Role;
import com.example.leavestech.service.dto.PermissionDTO;
import com.example.leavestech.service.dto.RoleDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Permission} and its DTO {@link PermissionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PermissionMapper extends EntityMapper<PermissionDTO, Permission> {
    @Mapping(target = "roles", source = "roles", qualifiedByName = "roleIdSet")
    PermissionDTO toDto(Permission s);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "removeRoles", ignore = true)
    Permission toEntity(PermissionDTO permissionDTO);

    @Named("roleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RoleDTO toDtoRoleId(Role role);

    @Named("roleIdSet")
    default Set<RoleDTO> toDtoRoleIdSet(Set<Role> role) {
        return role.stream().map(this::toDtoRoleId).collect(Collectors.toSet());
    }
}
