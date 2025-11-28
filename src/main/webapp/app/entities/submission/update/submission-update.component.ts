import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IStudent } from 'app/entities/student/student.model';
import { StudentService } from 'app/entities/student/service/student.service';
import { ICodingQuestion } from 'app/entities/coding-question/coding-question.model';
import { CodingQuestionService } from 'app/entities/coding-question/service/coding-question.service';
import { IExam } from 'app/entities/exam/exam.model';
import { ExamService } from 'app/entities/exam/service/exam.service';
import { SubmissionStatus } from 'app/entities/enumerations/submission-status.model';
import { SubmissionService } from '../service/submission.service';
import { ISubmission } from '../submission.model';
import { SubmissionFormGroup, SubmissionFormService } from './submission-form.service';

@Component({
  selector: 'jhi-submission-update',
  templateUrl: './submission-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SubmissionUpdateComponent implements OnInit {
  isSaving = false;
  submission: ISubmission | null = null;
  submissionStatusValues = Object.keys(SubmissionStatus);

  studentsSharedCollection: IStudent[] = [];
  codingQuestionsSharedCollection: ICodingQuestion[] = [];
  examsSharedCollection: IExam[] = [];

  protected submissionService = inject(SubmissionService);
  protected submissionFormService = inject(SubmissionFormService);
  protected studentService = inject(StudentService);
  protected codingQuestionService = inject(CodingQuestionService);
  protected examService = inject(ExamService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SubmissionFormGroup = this.submissionFormService.createSubmissionFormGroup();

  compareStudent = (o1: IStudent | null, o2: IStudent | null): boolean => this.studentService.compareStudent(o1, o2);

  compareCodingQuestion = (o1: ICodingQuestion | null, o2: ICodingQuestion | null): boolean =>
    this.codingQuestionService.compareCodingQuestion(o1, o2);

  compareExam = (o1: IExam | null, o2: IExam | null): boolean => this.examService.compareExam(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ submission }) => {
      this.submission = submission;
      if (submission) {
        this.updateForm(submission);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const submission = this.submissionFormService.getSubmission(this.editForm);
    if (submission.id !== null) {
      this.subscribeToSaveResponse(this.submissionService.update(submission));
    } else {
      this.subscribeToSaveResponse(this.submissionService.create(submission));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISubmission>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(submission: ISubmission): void {
    this.submission = submission;
    this.submissionFormService.resetForm(this.editForm, submission);

    this.studentsSharedCollection = this.studentService.addStudentToCollectionIfMissing<IStudent>(
      this.studentsSharedCollection,
      submission.student,
    );
    this.codingQuestionsSharedCollection = this.codingQuestionService.addCodingQuestionToCollectionIfMissing<ICodingQuestion>(
      this.codingQuestionsSharedCollection,
      submission.question,
    );
    this.examsSharedCollection = this.examService.addExamToCollectionIfMissing<IExam>(this.examsSharedCollection, submission.exam);
  }

  protected loadRelationshipsOptions(): void {
    this.studentService
      .query()
      .pipe(map((res: HttpResponse<IStudent[]>) => res.body ?? []))
      .pipe(
        map((students: IStudent[]) => this.studentService.addStudentToCollectionIfMissing<IStudent>(students, this.submission?.student)),
      )
      .subscribe((students: IStudent[]) => (this.studentsSharedCollection = students));

    this.codingQuestionService
      .query()
      .pipe(map((res: HttpResponse<ICodingQuestion[]>) => res.body ?? []))
      .pipe(
        map((codingQuestions: ICodingQuestion[]) =>
          this.codingQuestionService.addCodingQuestionToCollectionIfMissing<ICodingQuestion>(codingQuestions, this.submission?.question),
        ),
      )
      .subscribe((codingQuestions: ICodingQuestion[]) => (this.codingQuestionsSharedCollection = codingQuestions));

    this.examService
      .query()
      .pipe(map((res: HttpResponse<IExam[]>) => res.body ?? []))
      .pipe(map((exams: IExam[]) => this.examService.addExamToCollectionIfMissing<IExam>(exams, this.submission?.exam)))
      .subscribe((exams: IExam[]) => (this.examsSharedCollection = exams));
  }
}
