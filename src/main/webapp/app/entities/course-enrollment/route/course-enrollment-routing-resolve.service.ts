import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICourseEnrollment } from '../course-enrollment.model';
import { CourseEnrollmentService } from '../service/course-enrollment.service';

const courseEnrollmentResolve = (route: ActivatedRouteSnapshot): Observable<null | ICourseEnrollment> => {
  const id = route.params.id;
  if (id) {
    return inject(CourseEnrollmentService)
      .find(id)
      .pipe(
        mergeMap((courseEnrollment: HttpResponse<ICourseEnrollment>) => {
          if (courseEnrollment.body) {
            return of(courseEnrollment.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default courseEnrollmentResolve;
