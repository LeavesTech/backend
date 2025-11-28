import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CodingQuestionResolve from './route/coding-question-routing-resolve.service';

const codingQuestionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/coding-question.component').then(m => m.CodingQuestionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/coding-question-detail.component').then(m => m.CodingQuestionDetailComponent),
    resolve: {
      codingQuestion: CodingQuestionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/coding-question-update.component').then(m => m.CodingQuestionUpdateComponent),
    resolve: {
      codingQuestion: CodingQuestionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/coding-question-update.component').then(m => m.CodingQuestionUpdateComponent),
    resolve: {
      codingQuestion: CodingQuestionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default codingQuestionRoute;
