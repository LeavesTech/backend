package com.example.leavestech.service.mapper;

import com.example.leavestech.domain.Permission;
import com.example.leavestech.domain.Role;
import com.example.leavestech.domain.User;
import com.example.leavestech.service.dto.PermissionDTO;
import com.example.leavestech.service.dto.RoleDTO;
import com.example.leavestech.service.dto.UserDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Role} and its DTO {@link RoleDTO}.
 */
@Mapper(componentModel = "spring")
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {
    @Mapping(target = "permissions", source = "permissions", qualifiedByName = "permissionIdSet")
    @Mapping(target = "users", source = "users", qualifiedByName = "userIdSet")
    RoleDTO toDto(Role s);

    @Mapping(target = "removePermissions", ignore = true)
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

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("userIdSet")
    default Set<UserDTO> toDtoUserIdSet(Set<User> user) {
        return user.stream().map(this::toDtoUserId).collect(Collectors.toSet());
    }
}
