import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICourseEnrollment, NewCourseEnrollment } from '../course-enrollment.model';

export type PartialUpdateCourseEnrollment = Partial<ICourseEnrollment> & Pick<ICourseEnrollment, 'id'>;

export type EntityResponseType = HttpResponse<ICourseEnrollment>;
export type EntityArrayResponseType = HttpResponse<ICourseEnrollment[]>;

@Injectable({ providedIn: 'root' })
export class CourseEnrollmentService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/course-enrollments');

  create(courseEnrollment: NewCourseEnrollment): Observable<EntityResponseType> {
    return this.http.post<ICourseEnrollment>(this.resourceUrl, courseEnrollment, { observe: 'response' });
  }

  update(courseEnrollment: ICourseEnrollment): Observable<EntityResponseType> {
    return this.http.put<ICourseEnrollment>(
      `${this.resourceUrl}/${this.getCourseEnrollmentIdentifier(courseEnrollment)}`,
      courseEnrollment,
      { observe: 'response' },
    );
  }

  partialUpdate(courseEnrollment: PartialUpdateCourseEnrollment): Observable<EntityResponseType> {
    return this.http.patch<ICourseEnrollment>(
      `${this.resourceUrl}/${this.getCourseEnrollmentIdentifier(courseEnrollment)}`,
      courseEnrollment,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICourseEnrollment>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICourseEnrollment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCourseEnrollmentIdentifier(courseEnrollment: Pick<ICourseEnrollment, 'id'>): number {
    return courseEnrollment.id;
  }

  compareCourseEnrollment(o1: Pick<ICourseEnrollment, 'id'> | null, o2: Pick<ICourseEnrollment, 'id'> | null): boolean {
    return o1 && o2 ? this.getCourseEnrollmentIdentifier(o1) === this.getCourseEnrollmentIdentifier(o2) : o1 === o2;
  }

  addCourseEnrollmentToCollectionIfMissing<Type extends Pick<ICourseEnrollment, 'id'>>(
    courseEnrollmentCollection: Type[],
    ...courseEnrollmentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const courseEnrollments: Type[] = courseEnrollmentsToCheck.filter(isPresent);
    if (courseEnrollments.length > 0) {
      const courseEnrollmentCollectionIdentifiers = courseEnrollmentCollection.map(courseEnrollmentItem =>
        this.getCourseEnrollmentIdentifier(courseEnrollmentItem),
      );
      const courseEnrollmentsToAdd = courseEnrollments.filter(courseEnrollmentItem => {
        const courseEnrollmentIdentifier = this.getCourseEnrollmentIdentifier(courseEnrollmentItem);
        if (courseEnrollmentCollectionIdentifiers.includes(courseEnrollmentIdentifier)) {
          return false;
        }
        courseEnrollmentCollectionIdentifiers.push(courseEnrollmentIdentifier);
        return true;
      });
      return [...courseEnrollmentsToAdd, ...courseEnrollmentCollection];
    }
    return courseEnrollmentCollection;
  }
}
