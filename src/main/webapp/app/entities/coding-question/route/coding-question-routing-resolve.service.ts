import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICodingQuestion } from '../coding-question.model';
import { CodingQuestionService } from '../service/coding-question.service';

const codingQuestionResolve = (route: ActivatedRouteSnapshot): Observable<null | ICodingQuestion> => {
  const id = route.params.id;
  if (id) {
    return inject(CodingQuestionService)
      .find(id)
      .pipe(
        mergeMap((codingQuestion: HttpResponse<ICodingQuestion>) => {
          if (codingQuestion.body) {
            return of(codingQuestion.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default codingQuestionResolve;
