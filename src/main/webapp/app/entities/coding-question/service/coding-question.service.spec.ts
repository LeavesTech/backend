import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ICodingQuestion } from '../coding-question.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../coding-question.test-samples';

import { CodingQuestionService } from './coding-question.service';

const requireRestSample: ICodingQuestion = {
  ...sampleWithRequiredData,
};

describe('CodingQuestion Service', () => {
  let service: CodingQuestionService;
  let httpMock: HttpTestingController;
  let expectedResult: ICodingQuestion | ICodingQuestion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CodingQuestionService);
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

    it('should create a CodingQuestion', () => {
      const codingQuestion = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(codingQuestion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CodingQuestion', () => {
      const codingQuestion = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(codingQuestion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CodingQuestion', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CodingQuestion', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CodingQuestion', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCodingQuestionToCollectionIfMissing', () => {
      it('should add a CodingQuestion to an empty array', () => {
        const codingQuestion: ICodingQuestion = sampleWithRequiredData;
        expectedResult = service.addCodingQuestionToCollectionIfMissing([], codingQuestion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(codingQuestion);
      });

      it('should not add a CodingQuestion to an array that contains it', () => {
        const codingQuestion: ICodingQuestion = sampleWithRequiredData;
        const codingQuestionCollection: ICodingQuestion[] = [
          {
            ...codingQuestion,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCodingQuestionToCollectionIfMissing(codingQuestionCollection, codingQuestion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CodingQuestion to an array that doesn't contain it", () => {
        const codingQuestion: ICodingQuestion = sampleWithRequiredData;
        const codingQuestionCollection: ICodingQuestion[] = [sampleWithPartialData];
        expectedResult = service.addCodingQuestionToCollectionIfMissing(codingQuestionCollection, codingQuestion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(codingQuestion);
      });

      it('should add only unique CodingQuestion to an array', () => {
        const codingQuestionArray: ICodingQuestion[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const codingQuestionCollection: ICodingQuestion[] = [sampleWithRequiredData];
        expectedResult = service.addCodingQuestionToCollectionIfMissing(codingQuestionCollection, ...codingQuestionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const codingQuestion: ICodingQuestion = sampleWithRequiredData;
        const codingQuestion2: ICodingQuestion = sampleWithPartialData;
        expectedResult = service.addCodingQuestionToCollectionIfMissing([], codingQuestion, codingQuestion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(codingQuestion);
        expect(expectedResult).toContain(codingQuestion2);
      });

      it('should accept null and undefined values', () => {
        const codingQuestion: ICodingQuestion = sampleWithRequiredData;
        expectedResult = service.addCodingQuestionToCollectionIfMissing([], null, codingQuestion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(codingQuestion);
      });

      it('should return initial array if no CodingQuestion is added', () => {
        const codingQuestionCollection: ICodingQuestion[] = [sampleWithRequiredData];
        expectedResult = service.addCodingQuestionToCollectionIfMissing(codingQuestionCollection, undefined, null);
        expect(expectedResult).toEqual(codingQuestionCollection);
      });
    });

    describe('compareCodingQuestion', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCodingQuestion(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 2391 };
        const entity2 = null;

        const compareResult1 = service.compareCodingQuestion(entity1, entity2);
        const compareResult2 = service.compareCodingQuestion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 2391 };
        const entity2 = { id: 12666 };

        const compareResult1 = service.compareCodingQuestion(entity1, entity2);
        const compareResult2 = service.compareCodingQuestion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 2391 };
        const entity2 = { id: 2391 };

        const compareResult1 = service.compareCodingQuestion(entity1, entity2);
        const compareResult2 = service.compareCodingQuestion(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
