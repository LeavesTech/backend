import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../coding-question.test-samples';

import { CodingQuestionFormService } from './coding-question-form.service';

describe('CodingQuestion Form Service', () => {
  let service: CodingQuestionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CodingQuestionFormService);
  });

  describe('Service methods', () => {
    describe('createCodingQuestionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCodingQuestionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            description: expect.any(Object),
            maxScore: expect.any(Object),
            starterCode: expect.any(Object),
            language: expect.any(Object),
            exams: expect.any(Object),
          }),
        );
      });

      it('passing ICodingQuestion should create a new form with FormGroup', () => {
        const formGroup = service.createCodingQuestionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            description: expect.any(Object),
            maxScore: expect.any(Object),
            starterCode: expect.any(Object),
            language: expect.any(Object),
            exams: expect.any(Object),
          }),
        );
      });
    });

    describe('getCodingQuestion', () => {
      it('should return NewCodingQuestion for default CodingQuestion initial value', () => {
        const formGroup = service.createCodingQuestionFormGroup(sampleWithNewData);

        const codingQuestion = service.getCodingQuestion(formGroup) as any;

        expect(codingQuestion).toMatchObject(sampleWithNewData);
      });

      it('should return NewCodingQuestion for empty CodingQuestion initial value', () => {
        const formGroup = service.createCodingQuestionFormGroup();

        const codingQuestion = service.getCodingQuestion(formGroup) as any;

        expect(codingQuestion).toMatchObject({});
      });

      it('should return ICodingQuestion', () => {
        const formGroup = service.createCodingQuestionFormGroup(sampleWithRequiredData);

        const codingQuestion = service.getCodingQuestion(formGroup) as any;

        expect(codingQuestion).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICodingQuestion should not enable id FormControl', () => {
        const formGroup = service.createCodingQuestionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCodingQuestion should disable id FormControl', () => {
        const formGroup = service.createCodingQuestionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
