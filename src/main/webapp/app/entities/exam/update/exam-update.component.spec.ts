import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICourse } from 'app/entities/course/course.model';
import { CourseService } from 'app/entities/course/service/course.service';
import { ICodingQuestion } from 'app/entities/coding-question/coding-question.model';
import { CodingQuestionService } from 'app/entities/coding-question/service/coding-question.service';
import { IExam } from '../exam.model';
import { ExamService } from '../service/exam.service';
import { ExamFormService } from './exam-form.service';

import { ExamUpdateComponent } from './exam-update.component';

describe('Exam Management Update Component', () => {
  let comp: ExamUpdateComponent;
  let fixture: ComponentFixture<ExamUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let examFormService: ExamFormService;
  let examService: ExamService;
  let courseService: CourseService;
  let codingQuestionService: CodingQuestionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ExamUpdateComponent],
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
      .overrideTemplate(ExamUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExamUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    examFormService = TestBed.inject(ExamFormService);
    examService = TestBed.inject(ExamService);
    courseService = TestBed.inject(CourseService);
    codingQuestionService = TestBed.inject(CodingQuestionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Course query and add missing value', () => {
      const exam: IExam = { id: 13366 };
      const course: ICourse = { id: 2858 };
      exam.course = course;

      const courseCollection: ICourse[] = [{ id: 2858 }];
      jest.spyOn(courseService, 'query').mockReturnValue(of(new HttpResponse({ body: courseCollection })));
      const additionalCourses = [course];
      const expectedCollection: ICourse[] = [...additionalCourses, ...courseCollection];
      jest.spyOn(courseService, 'addCourseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ exam });
      comp.ngOnInit();

      expect(courseService.query).toHaveBeenCalled();
      expect(courseService.addCourseToCollectionIfMissing).toHaveBeenCalledWith(
        courseCollection,
        ...additionalCourses.map(expect.objectContaining),
      );
      expect(comp.coursesSharedCollection).toEqual(expectedCollection);
    });

    it('should call CodingQuestion query and add missing value', () => {
      const exam: IExam = { id: 13366 };
      const questions: ICodingQuestion[] = [{ id: 2391 }];
      exam.questions = questions;

      const codingQuestionCollection: ICodingQuestion[] = [{ id: 2391 }];
      jest.spyOn(codingQuestionService, 'query').mockReturnValue(of(new HttpResponse({ body: codingQuestionCollection })));
      const additionalCodingQuestions = [...questions];
      const expectedCollection: ICodingQuestion[] = [...additionalCodingQuestions, ...codingQuestionCollection];
      jest.spyOn(codingQuestionService, 'addCodingQuestionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ exam });
      comp.ngOnInit();

      expect(codingQuestionService.query).toHaveBeenCalled();
      expect(codingQuestionService.addCodingQuestionToCollectionIfMissing).toHaveBeenCalledWith(
        codingQuestionCollection,
        ...additionalCodingQuestions.map(expect.objectContaining),
      );
      expect(comp.codingQuestionsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const exam: IExam = { id: 13366 };
      const course: ICourse = { id: 2858 };
      exam.course = course;
      const questions: ICodingQuestion = { id: 2391 };
      exam.questions = [questions];

      activatedRoute.data = of({ exam });
      comp.ngOnInit();

      expect(comp.coursesSharedCollection).toContainEqual(course);
      expect(comp.codingQuestionsSharedCollection).toContainEqual(questions);
      expect(comp.exam).toEqual(exam);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExam>>();
      const exam = { id: 15727 };
      jest.spyOn(examFormService, 'getExam').mockReturnValue(exam);
      jest.spyOn(examService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ exam });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: exam }));
      saveSubject.complete();

      // THEN
      expect(examFormService.getExam).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(examService.update).toHaveBeenCalledWith(expect.objectContaining(exam));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExam>>();
      const exam = { id: 15727 };
      jest.spyOn(examFormService, 'getExam').mockReturnValue({ id: null });
      jest.spyOn(examService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ exam: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: exam }));
      saveSubject.complete();

      // THEN
      expect(examFormService.getExam).toHaveBeenCalled();
      expect(examService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExam>>();
      const exam = { id: 15727 };
      jest.spyOn(examService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ exam });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(examService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCourse', () => {
      it('should forward to courseService', () => {
        const entity = { id: 2858 };
        const entity2 = { id: 3722 };
        jest.spyOn(courseService, 'compareCourse');
        comp.compareCourse(entity, entity2);
        expect(courseService.compareCourse).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCodingQuestion', () => {
      it('should forward to codingQuestionService', () => {
        const entity = { id: 2391 };
        const entity2 = { id: 12666 };
        jest.spyOn(codingQuestionService, 'compareCodingQuestion');
        comp.compareCodingQuestion(entity, entity2);
        expect(codingQuestionService.compareCodingQuestion).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
