import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICourseEnrollment, NewCourseEnrollment } from '../course-enrollment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICourseEnrollment for edit and NewCourseEnrollmentFormGroupInput for create.
 */
type CourseEnrollmentFormGroupInput = ICourseEnrollment | PartialWithRequiredKeyOf<NewCourseEnrollment>;

type CourseEnrollmentFormDefaults = Pick<NewCourseEnrollment, 'id'>;

type CourseEnrollmentFormGroupContent = {
  id: FormControl<ICourseEnrollment['id'] | NewCourseEnrollment['id']>;
  status: FormControl<ICourseEnrollment['status']>;
  course: FormControl<ICourseEnrollment['course']>;
  student: FormControl<ICourseEnrollment['student']>;
};

export type CourseEnrollmentFormGroup = FormGroup<CourseEnrollmentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CourseEnrollmentFormService {
  createCourseEnrollmentFormGroup(courseEnrollment: CourseEnrollmentFormGroupInput = { id: null }): CourseEnrollmentFormGroup {
    const courseEnrollmentRawValue = {
      ...this.getFormDefaults(),
      ...courseEnrollment,
    };
    return new FormGroup<CourseEnrollmentFormGroupContent>({
      id: new FormControl(
        { value: courseEnrollmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      status: new FormControl(courseEnrollmentRawValue.status, {
        validators: [Validators.required],
      }),
      course: new FormControl(courseEnrollmentRawValue.course, {
        validators: [Validators.required],
      }),
      student: new FormControl(courseEnrollmentRawValue.student, {
        validators: [Validators.required],
      }),
    });
  }

  getCourseEnrollment(form: CourseEnrollmentFormGroup): ICourseEnrollment | NewCourseEnrollment {
    return form.getRawValue() as ICourseEnrollment | NewCourseEnrollment;
  }

  resetForm(form: CourseEnrollmentFormGroup, courseEnrollment: CourseEnrollmentFormGroupInput): void {
    const courseEnrollmentRawValue = { ...this.getFormDefaults(), ...courseEnrollment };
    form.reset(
      {
        ...courseEnrollmentRawValue,
        id: { value: courseEnrollmentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CourseEnrollmentFormDefaults {
    return {
      id: null,
    };
  }
}
