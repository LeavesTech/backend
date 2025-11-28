import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CourseEnrollmentResolve from './route/course-enrollment-routing-resolve.service';

const courseEnrollmentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/course-enrollment.component').then(m => m.CourseEnrollmentComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/course-enrollment-detail.component').then(m => m.CourseEnrollmentDetailComponent),
    resolve: {
      courseEnrollment: CourseEnrollmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/course-enrollment-update.component').then(m => m.CourseEnrollmentUpdateComponent),
    resolve: {
      courseEnrollment: CourseEnrollmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/course-enrollment-update.component').then(m => m.CourseEnrollmentUpdateComponent),
    resolve: {
      courseEnrollment: CourseEnrollmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default courseEnrollmentRoute;
