import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IStudent } from 'app/entities/student/student.model';
import { StudentService } from 'app/entities/student/service/student.service';
import { ICodingQuestion } from 'app/entities/coding-question/coding-question.model';
import { CodingQuestionService } from 'app/entities/coding-question/service/coding-question.service';
import { IExam } from 'app/entities/exam/exam.model';
import { ExamService } from 'app/entities/exam/service/exam.service';
import { ISubmission } from '../submission.model';
import { SubmissionService } from '../service/submission.service';
import { SubmissionFormService } from './submission-form.service';

import { SubmissionUpdateComponent } from './submission-update.component';

describe('Submission Management Update Component', () => {
  let comp: SubmissionUpdateComponent;
  let fixture: ComponentFixture<SubmissionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let submissionFormService: SubmissionFormService;
  let submissionService: SubmissionService;
  let studentService: StudentService;
  let codingQuestionService: CodingQuestionService;
  let examService: ExamService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SubmissionUpdateComponent],
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
      .overrideTemplate(SubmissionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SubmissionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    submissionFormService = TestBed.inject(SubmissionFormService);
    submissionService = TestBed.inject(SubmissionService);
    studentService = TestBed.inject(StudentService);
    codingQuestionService = TestBed.inject(CodingQuestionService);
    examService = TestBed.inject(ExamService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Student query and add missing value', () => {
      const submission: ISubmission = { id: 15044 };
      const student: IStudent = { id: 9978 };
      submission.student = student;

      const studentCollection: IStudent[] = [{ id: 9978 }];
      jest.spyOn(studentService, 'query').mockReturnValue(of(new HttpResponse({ body: studentCollection })));
      const additionalStudents = [student];
      const expectedCollection: IStudent[] = [...additionalStudents, ...studentCollection];
      jest.spyOn(studentService, 'addStudentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ submission });
      comp.ngOnInit();

      expect(studentService.query).toHaveBeenCalled();
      expect(studentService.addStudentToCollectionIfMissing).toHaveBeenCalledWith(
        studentCollection,
        ...additionalStudents.map(expect.objectContaining),
      );
      expect(comp.studentsSharedCollection).toEqual(expectedCollection);
    });

    it('should call CodingQuestion query and add missing value', () => {
      const submission: ISubmission = { id: 15044 };
      const question: ICodingQuestion = { id: 2391 };
      submission.question = question;

      const codingQuestionCollection: ICodingQuestion[] = [{ id: 2391 }];
      jest.spyOn(codingQuestionService, 'query').mockReturnValue(of(new HttpResponse({ body: codingQuestionCollection })));
      const additionalCodingQuestions = [question];
      const expectedCollection: ICodingQuestion[] = [...additionalCodingQuestions, ...codingQuestionCollection];
      jest.spyOn(codingQuestionService, 'addCodingQuestionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ submission });
      comp.ngOnInit();

      expect(codingQuestionService.query).toHaveBeenCalled();
      expect(codingQuestionService.addCodingQuestionToCollectionIfMissing).toHaveBeenCalledWith(
        codingQuestionCollection,
        ...additionalCodingQuestions.map(expect.objectContaining),
      );
      expect(comp.codingQuestionsSharedCollection).toEqual(expectedCollection);
    });

    it('should call Exam query and add missing value', () => {
      const submission: ISubmission = { id: 15044 };
      const exam: IExam = { id: 15727 };
      submission.exam = exam;

      const examCollection: IExam[] = [{ id: 15727 }];
      jest.spyOn(examService, 'query').mockReturnValue(of(new HttpResponse({ body: examCollection })));
      const additionalExams = [exam];
      const expectedCollection: IExam[] = [...additionalExams, ...examCollection];
      jest.spyOn(examService, 'addExamToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ submission });
      comp.ngOnInit();

      expect(examService.query).toHaveBeenCalled();
      expect(examService.addExamToCollectionIfMissing).toHaveBeenCalledWith(
        examCollection,
        ...additionalExams.map(expect.objectContaining),
      );
      expect(comp.examsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const submission: ISubmission = { id: 15044 };
      const student: IStudent = { id: 9978 };
      submission.student = student;
      const question: ICodingQuestion = { id: 2391 };
      submission.question = question;
      const exam: IExam = { id: 15727 };
      submission.exam = exam;

      activatedRoute.data = of({ submission });
      comp.ngOnInit();

      expect(comp.studentsSharedCollection).toContainEqual(student);
      expect(comp.codingQuestionsSharedCollection).toContainEqual(question);
      expect(comp.examsSharedCollection).toContainEqual(exam);
      expect(comp.submission).toEqual(submission);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubmission>>();
      const submission = { id: 27139 };
      jest.spyOn(submissionFormService, 'getSubmission').mockReturnValue(submission);
      jest.spyOn(submissionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ submission });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: submission }));
      saveSubject.complete();

      // THEN
      expect(submissionFormService.getSubmission).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(submissionService.update).toHaveBeenCalledWith(expect.objectContaining(submission));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubmission>>();
      const submission = { id: 27139 };
      jest.spyOn(submissionFormService, 'getSubmission').mockReturnValue({ id: null });
      jest.spyOn(submissionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ submission: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: submission }));
      saveSubject.complete();

      // THEN
      expect(submissionFormService.getSubmission).toHaveBeenCalled();
      expect(submissionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubmission>>();
      const submission = { id: 27139 };
      jest.spyOn(submissionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ submission });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(submissionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareStudent', () => {
      it('should forward to studentService', () => {
        const entity = { id: 9978 };
        const entity2 = { id: 22718 };
        jest.spyOn(studentService, 'compareStudent');
        comp.compareStudent(entity, entity2);
        expect(studentService.compareStudent).toHaveBeenCalledWith(entity, entity2);
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
