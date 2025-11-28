import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ICourseEnrollment } from '../course-enrollment.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../course-enrollment.test-samples';

import { CourseEnrollmentService } from './course-enrollment.service';

const requireRestSample: ICourseEnrollment = {
  ...sampleWithRequiredData,
};

describe('CourseEnrollment Service', () => {
  let service: CourseEnrollmentService;
  let httpMock: HttpTestingController;
  let expectedResult: ICourseEnrollment | ICourseEnrollment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CourseEnrollmentService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a CourseEnrollment', () => {
      const courseEnrollment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(courseEnrollment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CourseEnrollment', () => {
      const courseEnrollment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(courseEnrollment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CourseEnrollment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CourseEnrollment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CourseEnrollment', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCourseEnrollmentToCollectionIfMissing', () => {
      it('should add a CourseEnrollment to an empty array', () => {
        const courseEnrollment: ICourseEnrollment = sampleWithRequiredData;
        expectedResult = service.addCourseEnrollmentToCollectionIfMissing([], courseEnrollment);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(courseEnrollment);
      });

      it('should not add a CourseEnrollment to an array that contains it', () => {
        const courseEnrollment: ICourseEnrollment = sampleWithRequiredData;
        const courseEnrollmentCollection: ICourseEnrollment[] = [
          {
            ...courseEnrollment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCourseEnrollmentToCollectionIfMissing(courseEnrollmentCollection, courseEnrollment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CourseEnrollment to an array that doesn't contain it", () => {
        const courseEnrollment: ICourseEnrollment = sampleWithRequiredData;
        const courseEnrollmentCollection: ICourseEnrollment[] = [sampleWithPartialData];
        expectedResult = service.addCourseEnrollmentToCollectionIfMissing(courseEnrollmentCollection, courseEnrollment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(courseEnrollment);
      });

      it('should add only unique CourseEnrollment to an array', () => {
        const courseEnrollmentArray: ICourseEnrollment[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const courseEnrollmentCollection: ICourseEnrollment[] = [sampleWithRequiredData];
        expectedResult = service.addCourseEnrollmentToCollectionIfMissing(courseEnrollmentCollection, ...courseEnrollmentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const courseEnrollment: ICourseEnrollment = sampleWithRequiredData;
        const courseEnrollment2: ICourseEnrollment = sampleWithPartialData;
        expectedResult = service.addCourseEnrollmentToCollectionIfMissing([], courseEnrollment, courseEnrollment2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(courseEnrollment);
        expect(expectedResult).toContain(courseEnrollment2);
      });

      it('should accept null and undefined values', () => {
        const courseEnrollment: ICourseEnrollment = sampleWithRequiredData;
        expectedResult = service.addCourseEnrollmentToCollectionIfMissing([], null, courseEnrollment, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(courseEnrollment);
      });

      it('should return initial array if no CourseEnrollment is added', () => {
        const courseEnrollmentCollection: ICourseEnrollment[] = [sampleWithRequiredData];
        expectedResult = service.addCourseEnrollmentToCollectionIfMissing(courseEnrollmentCollection, undefined, null);
        expect(expectedResult).toEqual(courseEnrollmentCollection);
      });
    });

    describe('compareCourseEnrollment', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCourseEnrollment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 11487 };
        const entity2 = null;

        const compareResult1 = service.compareCourseEnrollment(entity1, entity2);
        const compareResult2 = service.compareCourseEnrollment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 11487 };
        const entity2 = { id: 8188 };

        const compareResult1 = service.compareCourseEnrollment(entity1, entity2);
        const compareResult2 = service.compareCourseEnrollment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 11487 };
        const entity2 = { id: 11487 };

        const compareResult1 = service.compareCourseEnrollment(entity1, entity2);
        const compareResult2 = service.compareCourseEnrollment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
