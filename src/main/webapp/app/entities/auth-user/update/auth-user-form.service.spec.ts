import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../auth-user.test-samples';

import { AuthUserFormService } from './auth-user-form.service';

describe('AuthUser Form Service', () => {
  let service: AuthUserFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuthUserFormService);
  });

  describe('Service methods', () => {
    describe('createAuthUserFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAuthUserFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            username: expect.any(Object),
            email: expect.any(Object),
            phoneNumber: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            password: expect.any(Object),
            enabled: expect.any(Object),
            userType: expect.any(Object),
            roles: expect.any(Object),
          }),
        );
      });

      it('passing IAuthUser should create a new form with FormGroup', () => {
        const formGroup = service.createAuthUserFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            username: expect.any(Object),
            email: expect.any(Object),
            phoneNumber: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            password: expect.any(Object),
            enabled: expect.any(Object),
            userType: expect.any(Object),
            roles: expect.any(Object),
          }),
        );
      });
    });

    describe('getAuthUser', () => {
      it('should return NewAuthUser for default AuthUser initial value', () => {
        const formGroup = service.createAuthUserFormGroup(sampleWithNewData);

        const authUser = service.getAuthUser(formGroup) as any;

        expect(authUser).toMatchObject(sampleWithNewData);
      });

      it('should return NewAuthUser for empty AuthUser initial value', () => {
        const formGroup = service.createAuthUserFormGroup();

        const authUser = service.getAuthUser(formGroup) as any;

        expect(authUser).toMatchObject({});
      });

      it('should return IAuthUser', () => {
        const formGroup = service.createAuthUserFormGroup(sampleWithRequiredData);

        const authUser = service.getAuthUser(formGroup) as any;

        expect(authUser).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAuthUser should not enable id FormControl', () => {
        const formGroup = service.createAuthUserFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAuthUser should disable id FormControl', () => {
        const formGroup = service.createAuthUserFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
