import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IExam, NewExam } from '../exam.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExam for edit and NewExamFormGroupInput for create.
 */
type ExamFormGroupInput = IExam | PartialWithRequiredKeyOf<NewExam>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IExam | NewExam> = Omit<T, 'startedAt' | 'endAt'> & {
  startedAt?: string | null;
  endAt?: string | null;
};

type ExamFormRawValue = FormValueOf<IExam>;

type NewExamFormRawValue = FormValueOf<NewExam>;

type ExamFormDefaults = Pick<NewExam, 'id' | 'startedAt' | 'endAt' | 'questions'>;

type ExamFormGroupContent = {
  id: FormControl<ExamFormRawValue['id'] | NewExam['id']>;
  title: FormControl<ExamFormRawValue['title']>;
  startedAt: FormControl<ExamFormRawValue['startedAt']>;
  endAt: FormControl<ExamFormRawValue['endAt']>;
  durationTime: FormControl<ExamFormRawValue['durationTime']>;
  type: FormControl<ExamFormRawValue['type']>;
  course: FormControl<ExamFormRawValue['course']>;
  questions: FormControl<ExamFormRawValue['questions']>;
};

export type ExamFormGroup = FormGroup<ExamFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExamFormService {
  createExamFormGroup(exam: ExamFormGroupInput = { id: null }): ExamFormGroup {
    const examRawValue = this.convertExamToExamRawValue({
      ...this.getFormDefaults(),
      ...exam,
    });
    return new FormGroup<ExamFormGroupContent>({
      id: new FormControl(
        { value: examRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(examRawValue.title, {
        validators: [Validators.required],
      }),
      startedAt: new FormControl(examRawValue.startedAt, {
        validators: [Validators.required],
      }),
      endAt: new FormControl(examRawValue.endAt, {
        validators: [Validators.required],
      }),
      durationTime: new FormControl(examRawValue.durationTime, {
        validators: [Validators.required],
      }),
      type: new FormControl(examRawValue.type, {
        validators: [Validators.required],
      }),
      course: new FormControl(examRawValue.course, {
        validators: [Validators.required],
      }),
      questions: new FormControl(examRawValue.questions ?? []),
    });
  }

  getExam(form: ExamFormGroup): IExam | NewExam {
    return this.convertExamRawValueToExam(form.getRawValue() as ExamFormRawValue | NewExamFormRawValue);
  }

  resetForm(form: ExamFormGroup, exam: ExamFormGroupInput): void {
    const examRawValue = this.convertExamToExamRawValue({ ...this.getFormDefaults(), ...exam });
    form.reset(
      {
        ...examRawValue,
        id: { value: examRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ExamFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startedAt: currentTime,
      endAt: currentTime,
      questions: [],
    };
  }

  private convertExamRawValueToExam(rawExam: ExamFormRawValue | NewExamFormRawValue): IExam | NewExam {
    return {
      ...rawExam,
      startedAt: dayjs(rawExam.startedAt, DATE_TIME_FORMAT),
      endAt: dayjs(rawExam.endAt, DATE_TIME_FORMAT),
    };
  }

  private convertExamToExamRawValue(
    exam: IExam | (Partial<NewExam> & ExamFormDefaults),
  ): ExamFormRawValue | PartialWithRequiredKeyOf<NewExamFormRawValue> {
    return {
      ...exam,
      startedAt: exam.startedAt ? exam.startedAt.format(DATE_TIME_FORMAT) : undefined,
      endAt: exam.endAt ? exam.endAt.format(DATE_TIME_FORMAT) : undefined,
      questions: exam.questions ?? [],
    };
  }
}
