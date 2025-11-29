package com.example.leavestech.domain;

import static com.example.leavestech.domain.CourseTestSamples.*;
import static com.example.leavestech.domain.DepartmentTestSamples.*;
import static com.example.leavestech.domain.TeacherTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.leavestech.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CourseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Course.class);
        Course course1 = getCourseSample1();
        Course course2 = new Course();
        assertThat(course1).isNotEqualTo(course2);

        course2.setId(course1.getId());
        assertThat(course1).isEqualTo(course2);

        course2 = getCourseSample2();
        assertThat(course1).isNotEqualTo(course2);
    }

    @Test
    void ownerTest() {
        Course course = getCourseRandomSampleGenerator();
        Teacher teacherBack = getTeacherRandomSampleGenerator();

        course.setOwner(teacherBack);
        assertThat(course.getOwner()).isEqualTo(teacherBack);

        course.owner(null);
        assertThat(course.getOwner()).isNull();
    }

    @Test
    void departmentTest() {
        Course course = getCourseRandomSampleGenerator();
        Department departmentBack = getDepartmentRandomSampleGenerator();

        course.setDepartment(departmentBack);
        assertThat(course.getDepartment()).isEqualTo(departmentBack);

        course.department(null);
        assertThat(course.getDepartment()).isNull();
    }
}
