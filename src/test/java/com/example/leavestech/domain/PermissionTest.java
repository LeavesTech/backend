package com.example.leavestech.domain;

import static com.example.leavestech.domain.PermissionTestSamples.*;
import static com.example.leavestech.domain.RoleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.leavestech.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PermissionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Permission.class);
        Permission permission1 = getPermissionSample1();
        Permission permission2 = new Permission();
        assertThat(permission1).isNotEqualTo(permission2);

        permission2.setId(permission1.getId());
        assertThat(permission1).isEqualTo(permission2);

        permission2 = getPermissionSample2();
        assertThat(permission1).isNotEqualTo(permission2);
    }

    @Test
    void rolesTest() {
        Permission permission = getPermissionRandomSampleGenerator();
        Role roleBack = getRoleRandomSampleGenerator();

        permission.addRoles(roleBack);
        assertThat(permission.getRoles()).containsOnly(roleBack);
        assertThat(roleBack.getPermissions()).containsOnly(permission);

        permission.removeRoles(roleBack);
        assertThat(permission.getRoles()).doesNotContain(roleBack);
        assertThat(roleBack.getPermissions()).doesNotContain(permission);

        permission.roles(new HashSet<>(Set.of(roleBack)));
        assertThat(permission.getRoles()).containsOnly(roleBack);
        assertThat(roleBack.getPermissions()).containsOnly(permission);

        permission.setRoles(new HashSet<>());
        assertThat(permission.getRoles()).doesNotContain(roleBack);
        assertThat(roleBack.getPermissions()).doesNotContain(permission);
    }
}
