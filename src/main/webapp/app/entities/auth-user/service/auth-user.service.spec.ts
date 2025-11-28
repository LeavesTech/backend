import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAuthUser } from '../auth-user.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../auth-user.test-samples';

import { AuthUserService } from './auth-user.service';

const requireRestSample: IAuthUser = {
  ...sampleWithRequiredData,
};

describe('AuthUser Service', () => {
  let service: AuthUserService;
  let httpMock: HttpTestingController;
  let expectedResult: IAuthUser | IAuthUser[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AuthUserService);
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

    it('should create a AuthUser', () => {
      const authUser = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(authUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AuthUser', () => {
      const authUser = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(authUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AuthUser', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AuthUser', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AuthUser', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAuthUserToCollectionIfMissing', () => {
      it('should add a AuthUser to an empty array', () => {
        const authUser: IAuthUser = sampleWithRequiredData;
        expectedResult = service.addAuthUserToCollectionIfMissing([], authUser);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(authUser);
      });

      it('should not add a AuthUser to an array that contains it', () => {
        const authUser: IAuthUser = sampleWithRequiredData;
        const authUserCollection: IAuthUser[] = [
          {
            ...authUser,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAuthUserToCollectionIfMissing(authUserCollection, authUser);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AuthUser to an array that doesn't contain it", () => {
        const authUser: IAuthUser = sampleWithRequiredData;
        const authUserCollection: IAuthUser[] = [sampleWithPartialData];
        expectedResult = service.addAuthUserToCollectionIfMissing(authUserCollection, authUser);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(authUser);
      });

      it('should add only unique AuthUser to an array', () => {
        const authUserArray: IAuthUser[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const authUserCollection: IAuthUser[] = [sampleWithRequiredData];
        expectedResult = service.addAuthUserToCollectionIfMissing(authUserCollection, ...authUserArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const authUser: IAuthUser = sampleWithRequiredData;
        const authUser2: IAuthUser = sampleWithPartialData;
        expectedResult = service.addAuthUserToCollectionIfMissing([], authUser, authUser2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(authUser);
        expect(expectedResult).toContain(authUser2);
      });

      it('should accept null and undefined values', () => {
        const authUser: IAuthUser = sampleWithRequiredData;
        expectedResult = service.addAuthUserToCollectionIfMissing([], null, authUser, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(authUser);
      });

      it('should return initial array if no AuthUser is added', () => {
        const authUserCollection: IAuthUser[] = [sampleWithRequiredData];
        expectedResult = service.addAuthUserToCollectionIfMissing(authUserCollection, undefined, null);
        expect(expectedResult).toEqual(authUserCollection);
      });
    });

    describe('compareAuthUser', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAuthUser(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 4445 };
        const entity2 = null;

        const compareResult1 = service.compareAuthUser(entity1, entity2);
        const compareResult2 = service.compareAuthUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 4445 };
        const entity2 = { id: 3303 };

        const compareResult1 = service.compareAuthUser(entity1, entity2);
        const compareResult2 = service.compareAuthUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 4445 };
        const entity2 = { id: 4445 };

        const compareResult1 = service.compareAuthUser(entity1, entity2);
        const compareResult2 = service.compareAuthUser(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
