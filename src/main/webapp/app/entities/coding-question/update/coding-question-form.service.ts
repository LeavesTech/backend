import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICodingQuestion, NewCodingQuestion } from '../coding-question.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICodingQuestion for edit and NewCodingQuestionFormGroupInput for create.
 */
type CodingQuestionFormGroupInput = ICodingQuestion | PartialWithRequiredKeyOf<NewCodingQuestion>;

type CodingQuestionFormDefaults = Pick<NewCodingQuestion, 'id' | 'exams'>;

type CodingQuestionFormGroupContent = {
  id: FormControl<ICodingQuestion['id'] | NewCodingQuestion['id']>;
  title: FormControl<ICodingQuestion['title']>;
  description: FormControl<ICodingQuestion['description']>;
  maxScore: FormControl<ICodingQuestion['maxScore']>;
  starterCode: FormControl<ICodingQuestion['starterCode']>;
  language: FormControl<ICodingQuestion['language']>;
  exams: FormControl<ICodingQuestion['exams']>;
};

export type CodingQuestionFormGroup = FormGroup<CodingQuestionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CodingQuestionFormService {
  createCodingQuestionFormGroup(codingQuestion: CodingQuestionFormGroupInput = { id: null }): CodingQuestionFormGroup {
    const codingQuestionRawValue = {
      ...this.getFormDefaults(),
      ...codingQuestion,
    };
    return new FormGroup<CodingQuestionFormGroupContent>({
      id: new FormControl(
        { value: codingQuestionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(codingQuestionRawValue.title, {
        validators: [Validators.required],
      }),
      description: new FormControl(codingQuestionRawValue.description, {
        validators: [Validators.required],
      }),
      maxScore: new FormControl(codingQuestionRawValue.maxScore, {
        validators: [Validators.required],
      }),
      starterCode: new FormControl(codingQuestionRawValue.starterCode, {
        validators: [Validators.required],
      }),
      language: new FormControl(codingQuestionRawValue.language, {
        validators: [Validators.required],
      }),
      exams: new FormControl(codingQuestionRawValue.exams ?? []),
    });
  }

  getCodingQuestion(form: CodingQuestionFormGroup): ICodingQuestion | NewCodingQuestion {
    return form.getRawValue() as ICodingQuestion | NewCodingQuestion;
  }

  resetForm(form: CodingQuestionFormGroup, codingQuestion: CodingQuestionFormGroupInput): void {
    const codingQuestionRawValue = { ...this.getFormDefaults(), ...codingQuestion };
    form.reset(
      {
        ...codingQuestionRawValue,
        id: { value: codingQuestionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CodingQuestionFormDefaults {
    return {
      id: null,
      exams: [],
    };
  }
}
