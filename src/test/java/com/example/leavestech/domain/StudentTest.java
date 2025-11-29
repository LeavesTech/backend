package com.example.leavestech.domain;

import static com.example.leavestech.domain.DepartmentTestSamples.*;
import static com.example.leavestech.domain.StudentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.leavestech.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Student.class);
        Student student1 = getStudentSample1();
        Student student2 = new Student();
        assertThat(student1).isNotEqualTo(student2);

        student2.setId(student1.getId());
        assertThat(student1).isEqualTo(student2);

        student2 = getStudentSample2();
        assertThat(student1).isNotEqualTo(student2);
    }

    @Test
    void departmentTest() {
        Student student = getStudentRandomSampleGenerator();
        Department departmentBack = getDepartmentRandomSampleGenerator();

        student.setDepartment(departmentBack);
        assertThat(student.getDepartment()).isEqualTo(departmentBack);

        student.department(null);
        assertThat(student.getDepartment()).isNull();
    }
}
