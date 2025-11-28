import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../course-enrollment.test-samples';

import { CourseEnrollmentFormService } from './course-enrollment-form.service';

describe('CourseEnrollment Form Service', () => {
  let service: CourseEnrollmentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CourseEnrollmentFormService);
  });

  describe('Service methods', () => {
    describe('createCourseEnrollmentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCourseEnrollmentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            status: expect.any(Object),
            course: expect.any(Object),
            student: expect.any(Object),
          }),
        );
      });

      it('passing ICourseEnrollment should create a new form with FormGroup', () => {
        const formGroup = service.createCourseEnrollmentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            status: expect.any(Object),
            course: expect.any(Object),
            student: expect.any(Object),
          }),
        );
      });
    });

    describe('getCourseEnrollment', () => {
      it('should return NewCourseEnrollment for default CourseEnrollment initial value', () => {
        const formGroup = service.createCourseEnrollmentFormGroup(sampleWithNewData);

        const courseEnrollment = service.getCourseEnrollment(formGroup) as any;

        expect(courseEnrollment).toMatchObject(sampleWithNewData);
      });

      it('should return NewCourseEnrollment for empty CourseEnrollment initial value', () => {
        const formGroup = service.createCourseEnrollmentFormGroup();

        const courseEnrollment = service.getCourseEnrollment(formGroup) as any;

        expect(courseEnrollment).toMatchObject({});
      });

      it('should return ICourseEnrollment', () => {
        const formGroup = service.createCourseEnrollmentFormGroup(sampleWithRequiredData);

        const courseEnrollment = service.getCourseEnrollment(formGroup) as any;

        expect(courseEnrollment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICourseEnrollment should not enable id FormControl', () => {
        const formGroup = service.createCourseEnrollmentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCourseEnrollment should disable id FormControl', () => {
        const formGroup = service.createCourseEnrollmentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
