package com.example.leavestech.service.mapper;

import com.example.leavestech.domain.AuthUser;
import com.example.leavestech.domain.Permission;
import com.example.leavestech.domain.Role;
import com.example.leavestech.service.dto.AuthUserDTO;
import com.example.leavestech.service.dto.PermissionDTO;
import com.example.leavestech.service.dto.RoleDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Role} and its DTO {@link RoleDTO}.
 */
@Mapper(componentModel = "spring")
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {
    @Mapping(target = "permissions", source = "permissions", qualifiedByName = "permissionIdSet")
    @Mapping(target = "users", source = "users", qualifiedByName = "authUserIdSet")
    RoleDTO toDto(Role s);

    @Mapping(target = "removePermissions", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "removeUsers", ignore = true)
    Role toEntity(RoleDTO roleDTO);

    @Named("permissionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PermissionDTO toDtoPermissionId(Permission permission);

    @Named("permissionIdSet")
    default Set<PermissionDTO> toDtoPermissionIdSet(Set<Permission> permission) {
        return permission.stream().map(this::toDtoPermissionId).collect(Collectors.toSet());
    }

    @Named("authUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AuthUserDTO toDtoAuthUserId(AuthUser authUser);

    @Named("authUserIdSet")
    default Set<AuthUserDTO> toDtoAuthUserIdSet(Set<AuthUser> authUser) {
        return authUser.stream().map(this::toDtoAuthUserId).collect(Collectors.toSet());
    }
}
