package com.example.leavestech.domain;

import static com.example.leavestech.domain.AuthUserTestSamples.*;
import static com.example.leavestech.domain.RoleTestSamples.*;
import static com.example.leavestech.domain.StudentTestSamples.*;
import static com.example.leavestech.domain.TeacherTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.leavestech.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AuthUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuthUser.class);
        AuthUser authUser1 = getAuthUserSample1();
        AuthUser authUser2 = new AuthUser();
        assertThat(authUser1).isNotEqualTo(authUser2);

        authUser2.setId(authUser1.getId());
        assertThat(authUser1).isEqualTo(authUser2);

        authUser2 = getAuthUserSample2();
        assertThat(authUser1).isNotEqualTo(authUser2);
    }

    @Test
    void rolesTest() {
        AuthUser authUser = getAuthUserRandomSampleGenerator();
        Role roleBack = getRoleRandomSampleGenerator();

        authUser.addRoles(roleBack);
        assertThat(authUser.getRoles()).containsOnly(roleBack);

        authUser.removeRoles(roleBack);
        assertThat(authUser.getRoles()).doesNotContain(roleBack);

        authUser.roles(new HashSet<>(Set.of(roleBack)));
        assertThat(authUser.getRoles()).containsOnly(roleBack);

        authUser.setRoles(new HashSet<>());
        assertThat(authUser.getRoles()).doesNotContain(roleBack);
    }

    @Test
    void studentTest() {
        AuthUser authUser = getAuthUserRandomSampleGenerator();
        Student studentBack = getStudentRandomSampleGenerator();

        authUser.setStudent(studentBack);
        assertThat(authUser.getStudent()).isEqualTo(studentBack);
        assertThat(studentBack.getUser()).isEqualTo(authUser);

        authUser.student(null);
        assertThat(authUser.getStudent()).isNull();
        assertThat(studentBack.getUser()).isNull();
    }

    @Test
    void teacherTest() {
        AuthUser authUser = getAuthUserRandomSampleGenerator();
        Teacher teacherBack = getTeacherRandomSampleGenerator();

        authUser.setTeacher(teacherBack);
        assertThat(authUser.getTeacher()).isEqualTo(teacherBack);
        assertThat(teacherBack.getUser()).isEqualTo(authUser);

        authUser.teacher(null);
        assertThat(authUser.getTeacher()).isNull();
        assertThat(teacherBack.getUser()).isNull();
    }
}
