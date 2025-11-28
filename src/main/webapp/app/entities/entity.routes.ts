import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'auth-user',
    data: { pageTitle: 'leavesTechExamApp.authUser.home.title' },
    loadChildren: () => import('./auth-user/auth-user.routes'),
  },
  {
    path: 'student',
    data: { pageTitle: 'leavesTechExamApp.student.home.title' },
    loadChildren: () => import('./student/student.routes'),
  },
  {
    path: 'teacher',
    data: { pageTitle: 'leavesTechExamApp.teacher.home.title' },
    loadChildren: () => import('./teacher/teacher.routes'),
  },
  {
    path: 'department',
    data: { pageTitle: 'leavesTechExamApp.department.home.title' },
    loadChildren: () => import('./department/department.routes'),
  },
  {
    path: 'permission',
    data: { pageTitle: 'leavesTechExamApp.permission.home.title' },
    loadChildren: () => import('./permission/permission.routes'),
  },
  {
    path: 'role',
    data: { pageTitle: 'leavesTechExamApp.role.home.title' },
    loadChildren: () => import('./role/role.routes'),
  },
  {
    path: 'course',
    data: { pageTitle: 'leavesTechExamApp.course.home.title' },
    loadChildren: () => import('./course/course.routes'),
  },
  {
    path: 'course-enrollment',
    data: { pageTitle: 'leavesTechExamApp.courseEnrollment.home.title' },
    loadChildren: () => import('./course-enrollment/course-enrollment.routes'),
  },
  {
    path: 'exam',
    data: { pageTitle: 'leavesTechExamApp.exam.home.title' },
    loadChildren: () => import('./exam/exam.routes'),
  },
  {
    path: 'coding-question',
    data: { pageTitle: 'leavesTechExamApp.codingQuestion.home.title' },
    loadChildren: () => import('./coding-question/coding-question.routes'),
  },
  {
    path: 'submission',
    data: { pageTitle: 'leavesTechExamApp.submission.home.title' },
    loadChildren: () => import('./submission/submission.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
