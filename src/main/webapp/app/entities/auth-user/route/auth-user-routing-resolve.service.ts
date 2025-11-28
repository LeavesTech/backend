import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAuthUser } from '../auth-user.model';
import { AuthUserService } from '../service/auth-user.service';

const authUserResolve = (route: ActivatedRouteSnapshot): Observable<null | IAuthUser> => {
  const id = route.params.id;
  if (id) {
    return inject(AuthUserService)
      .find(id)
      .pipe(
        mergeMap((authUser: HttpResponse<IAuthUser>) => {
          if (authUser.body) {
            return of(authUser.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default authUserResolve;
