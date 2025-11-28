import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICourse } from 'app/entities/course/course.model';
import { CourseService } from 'app/entities/course/service/course.service';
import { ICodingQuestion } from 'app/entities/coding-question/coding-question.model';
import { CodingQuestionService } from 'app/entities/coding-question/service/coding-question.service';
import { ExamType } from 'app/entities/enumerations/exam-type.model';
import { ExamService } from '../service/exam.service';
import { IExam } from '../exam.model';
import { ExamFormGroup, ExamFormService } from './exam-form.service';

@Component({
  selector: 'jhi-exam-update',
  templateUrl: './exam-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ExamUpdateComponent implements OnInit {
  isSaving = false;
  exam: IExam | null = null;
  examTypeValues = Object.keys(ExamType);

  coursesSharedCollection: ICourse[] = [];
  codingQuestionsSharedCollection: ICodingQuestion[] = [];

  protected examService = inject(ExamService);
  protected examFormService = inject(ExamFormService);
  protected courseService = inject(CourseService);
  protected codingQuestionService = inject(CodingQuestionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ExamFormGroup = this.examFormService.createExamFormGroup();

  compareCourse = (o1: ICourse | null, o2: ICourse | null): boolean => this.courseService.compareCourse(o1, o2);

  compareCodingQuestion = (o1: ICodingQuestion | null, o2: ICodingQuestion | null): boolean =>
    this.codingQuestionService.compareCodingQuestion(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exam }) => {
      this.exam = exam;
      if (exam) {
        this.updateForm(exam);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const exam = this.examFormService.getExam(this.editForm);
    if (exam.id !== null) {
      this.subscribeToSaveResponse(this.examService.update(exam));
    } else {
      this.subscribeToSaveResponse(this.examService.create(exam));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExam>>): void {
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

  protected updateForm(exam: IExam): void {
    this.exam = exam;
    this.examFormService.resetForm(this.editForm, exam);

    this.coursesSharedCollection = this.courseService.addCourseToCollectionIfMissing<ICourse>(this.coursesSharedCollection, exam.course);
    this.codingQuestionsSharedCollection = this.codingQuestionService.addCodingQuestionToCollectionIfMissing<ICodingQuestion>(
      this.codingQuestionsSharedCollection,
      ...(exam.questions ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.courseService
      .query()
      .pipe(map((res: HttpResponse<ICourse[]>) => res.body ?? []))
      .pipe(map((courses: ICourse[]) => this.courseService.addCourseToCollectionIfMissing<ICourse>(courses, this.exam?.course)))
      .subscribe((courses: ICourse[]) => (this.coursesSharedCollection = courses));

    this.codingQuestionService
      .query()
      .pipe(map((res: HttpResponse<ICodingQuestion[]>) => res.body ?? []))
      .pipe(
        map((codingQuestions: ICodingQuestion[]) =>
          this.codingQuestionService.addCodingQuestionToCollectionIfMissing<ICodingQuestion>(
            codingQuestions,
            ...(this.exam?.questions ?? []),
          ),
        ),
      )
      .subscribe((codingQuestions: ICodingQuestion[]) => (this.codingQuestionsSharedCollection = codingQuestions));
  }
}
