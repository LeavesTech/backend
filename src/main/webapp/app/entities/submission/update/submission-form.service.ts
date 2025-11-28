import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISubmission, NewSubmission } from '../submission.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISubmission for edit and NewSubmissionFormGroupInput for create.
 */
type SubmissionFormGroupInput = ISubmission | PartialWithRequiredKeyOf<NewSubmission>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISubmission | NewSubmission> = Omit<T, 'submissionDate'> & {
  submissionDate?: string | null;
};

type SubmissionFormRawValue = FormValueOf<ISubmission>;

type NewSubmissionFormRawValue = FormValueOf<NewSubmission>;

type SubmissionFormDefaults = Pick<NewSubmission, 'id' | 'submissionDate'>;

type SubmissionFormGroupContent = {
  id: FormControl<SubmissionFormRawValue['id'] | NewSubmission['id']>;
  submittedCode: FormControl<SubmissionFormRawValue['submittedCode']>;
  score: FormControl<SubmissionFormRawValue['score']>;
  status: FormControl<SubmissionFormRawValue['status']>;
  submissionDate: FormControl<SubmissionFormRawValue['submissionDate']>;
  student: FormControl<SubmissionFormRawValue['student']>;
  question: FormControl<SubmissionFormRawValue['question']>;
  exam: FormControl<SubmissionFormRawValue['exam']>;
};

export type SubmissionFormGroup = FormGroup<SubmissionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SubmissionFormService {
  createSubmissionFormGroup(submission: SubmissionFormGroupInput = { id: null }): SubmissionFormGroup {
    const submissionRawValue = this.convertSubmissionToSubmissionRawValue({
      ...this.getFormDefaults(),
      ...submission,
    });
    return new FormGroup<SubmissionFormGroupContent>({
      id: new FormControl(
        { value: submissionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      submittedCode: new FormControl(submissionRawValue.submittedCode, {
        validators: [Validators.required],
      }),
      score: new FormControl(submissionRawValue.score),
      status: new FormControl(submissionRawValue.status, {
        validators: [Validators.required],
      }),
      submissionDate: new FormControl(submissionRawValue.submissionDate, {
        validators: [Validators.required],
      }),
      student: new FormControl(submissionRawValue.student, {
        validators: [Validators.required],
      }),
      question: new FormControl(submissionRawValue.question, {
        validators: [Validators.required],
      }),
      exam: new FormControl(submissionRawValue.exam, {
        validators: [Validators.required],
      }),
    });
  }

  getSubmission(form: SubmissionFormGroup): ISubmission | NewSubmission {
    return this.convertSubmissionRawValueToSubmission(form.getRawValue() as SubmissionFormRawValue | NewSubmissionFormRawValue);
  }

  resetForm(form: SubmissionFormGroup, submission: SubmissionFormGroupInput): void {
    const submissionRawValue = this.convertSubmissionToSubmissionRawValue({ ...this.getFormDefaults(), ...submission });
    form.reset(
      {
        ...submissionRawValue,
        id: { value: submissionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SubmissionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      submissionDate: currentTime,
    };
  }

  private convertSubmissionRawValueToSubmission(
    rawSubmission: SubmissionFormRawValue | NewSubmissionFormRawValue,
  ): ISubmission | NewSubmission {
    return {
      ...rawSubmission,
      submissionDate: dayjs(rawSubmission.submissionDate, DATE_TIME_FORMAT),
    };
  }

  private convertSubmissionToSubmissionRawValue(
    submission: ISubmission | (Partial<NewSubmission> & SubmissionFormDefaults),
  ): SubmissionFormRawValue | PartialWithRequiredKeyOf<NewSubmissionFormRawValue> {
    return {
      ...submission,
      submissionDate: submission.submissionDate ? submission.submissionDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
