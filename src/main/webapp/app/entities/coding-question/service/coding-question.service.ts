import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICodingQuestion, NewCodingQuestion } from '../coding-question.model';

export type PartialUpdateCodingQuestion = Partial<ICodingQuestion> & Pick<ICodingQuestion, 'id'>;

export type EntityResponseType = HttpResponse<ICodingQuestion>;
export type EntityArrayResponseType = HttpResponse<ICodingQuestion[]>;

@Injectable({ providedIn: 'root' })
export class CodingQuestionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/coding-questions');

  create(codingQuestion: NewCodingQuestion): Observable<EntityResponseType> {
    return this.http.post<ICodingQuestion>(this.resourceUrl, codingQuestion, { observe: 'response' });
  }

  update(codingQuestion: ICodingQuestion): Observable<EntityResponseType> {
    return this.http.put<ICodingQuestion>(`${this.resourceUrl}/${this.getCodingQuestionIdentifier(codingQuestion)}`, codingQuestion, {
      observe: 'response',
    });
  }

  partialUpdate(codingQuestion: PartialUpdateCodingQuestion): Observable<EntityResponseType> {
    return this.http.patch<ICodingQuestion>(`${this.resourceUrl}/${this.getCodingQuestionIdentifier(codingQuestion)}`, codingQuestion, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICodingQuestion>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICodingQuestion[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCodingQuestionIdentifier(codingQuestion: Pick<ICodingQuestion, 'id'>): number {
    return codingQuestion.id;
  }

  compareCodingQuestion(o1: Pick<ICodingQuestion, 'id'> | null, o2: Pick<ICodingQuestion, 'id'> | null): boolean {
    return o1 && o2 ? this.getCodingQuestionIdentifier(o1) === this.getCodingQuestionIdentifier(o2) : o1 === o2;
  }

  addCodingQuestionToCollectionIfMissing<Type extends Pick<ICodingQuestion, 'id'>>(
    codingQuestionCollection: Type[],
    ...codingQuestionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const codingQuestions: Type[] = codingQuestionsToCheck.filter(isPresent);
    if (codingQuestions.length > 0) {
      const codingQuestionCollectionIdentifiers = codingQuestionCollection.map(codingQuestionItem =>
        this.getCodingQuestionIdentifier(codingQuestionItem),
      );
      const codingQuestionsToAdd = codingQuestions.filter(codingQuestionItem => {
        const codingQuestionIdentifier = this.getCodingQuestionIdentifier(codingQuestionItem);
        if (codingQuestionCollectionIdentifiers.includes(codingQuestionIdentifier)) {
          return false;
        }
        codingQuestionCollectionIdentifiers.push(codingQuestionIdentifier);
        return true;
      });
      return [...codingQuestionsToAdd, ...codingQuestionCollection];
    }
    return codingQuestionCollection;
  }
}
