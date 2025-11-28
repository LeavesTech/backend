import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AuthUserResolve from './route/auth-user-routing-resolve.service';

const authUserRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/auth-user.component').then(m => m.AuthUserComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/auth-user-detail.component').then(m => m.AuthUserDetailComponent),
    resolve: {
      authUser: AuthUserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/auth-user-update.component').then(m => m.AuthUserUpdateComponent),
    resolve: {
      authUser: AuthUserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/auth-user-update.component').then(m => m.AuthUserUpdateComponent),
    resolve: {
      authUser: AuthUserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default authUserRoute;
