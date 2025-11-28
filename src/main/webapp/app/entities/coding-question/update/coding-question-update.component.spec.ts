import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IExam } from 'app/entities/exam/exam.model';
import { ExamService } from 'app/entities/exam/service/exam.service';
import { CodingQuestionService } from '../service/coding-question.service';
import { ICodingQuestion } from '../coding-question.model';
import { CodingQuestionFormService } from './coding-question-form.service';

import { CodingQuestionUpdateComponent } from './coding-question-update.component';

describe('CodingQuestion Management Update Component', () => {
  let comp: CodingQuestionUpdateComponent;
  let fixture: ComponentFixture<CodingQuestionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let codingQuestionFormService: CodingQuestionFormService;
  let codingQuestionService: CodingQuestionService;
  let examService: ExamService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CodingQuestionUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CodingQuestionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CodingQuestionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    codingQuestionFormService = TestBed.inject(CodingQuestionFormService);
    codingQuestionService = TestBed.inject(CodingQuestionService);
    examService = TestBed.inject(ExamService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Exam query and add missing value', () => {
      const codingQuestion: ICodingQuestion = { id: 12666 };
      const exams: IExam[] = [{ id: 15727 }];
      codingQuestion.exams = exams;

      const examCollection: IExam[] = [{ id: 15727 }];
      jest.spyOn(examService, 'query').mockReturnValue(of(new HttpResponse({ body: examCollection })));
      const additionalExams = [...exams];
      const expectedCollection: IExam[] = [...additionalExams, ...examCollection];
      jest.spyOn(examService, 'addExamToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ codingQuestion });
      comp.ngOnInit();

      expect(examService.query).toHaveBeenCalled();
      expect(examService.addExamToCollectionIfMissing).toHaveBeenCalledWith(
        examCollection,
        ...additionalExams.map(expect.objectContaining),
      );
      expect(comp.examsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const codingQuestion: ICodingQuestion = { id: 12666 };
      const exams: IExam = { id: 15727 };
      codingQuestion.exams = [exams];

      activatedRoute.data = of({ codingQuestion });
      comp.ngOnInit();

      expect(comp.examsSharedCollection).toContainEqual(exams);
      expect(comp.codingQuestion).toEqual(codingQuestion);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICodingQuestion>>();
      const codingQuestion = { id: 2391 };
      jest.spyOn(codingQuestionFormService, 'getCodingQuestion').mockReturnValue(codingQuestion);
      jest.spyOn(codingQuestionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ codingQuestion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: codingQuestion }));
      saveSubject.complete();

      // THEN
      expect(codingQuestionFormService.getCodingQuestion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(codingQuestionService.update).toHaveBeenCalledWith(expect.objectContaining(codingQuestion));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICodingQuestion>>();
      const codingQuestion = { id: 2391 };
      jest.spyOn(codingQuestionFormService, 'getCodingQuestion').mockReturnValue({ id: null });
      jest.spyOn(codingQuestionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ codingQuestion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: codingQuestion }));
      saveSubject.complete();

      // THEN
      expect(codingQuestionFormService.getCodingQuestion).toHaveBeenCalled();
      expect(codingQuestionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICodingQuestion>>();
      const codingQuestion = { id: 2391 };
      jest.spyOn(codingQuestionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ codingQuestion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(codingQuestionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareExam', () => {
      it('should forward to examService', () => {
        const entity = { id: 15727 };
        const entity2 = { id: 13366 };
        jest.spyOn(examService, 'compareExam');
        comp.compareExam(entity, entity2);
        expect(examService.compareExam).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
