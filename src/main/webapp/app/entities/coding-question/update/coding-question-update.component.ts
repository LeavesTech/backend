import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IExam } from 'app/entities/exam/exam.model';
import { ExamService } from 'app/entities/exam/service/exam.service';
import { ProgrammingLanguage } from 'app/entities/enumerations/programming-language.model';
import { CodingQuestionService } from '../service/coding-question.service';
import { ICodingQuestion } from '../coding-question.model';
import { CodingQuestionFormGroup, CodingQuestionFormService } from './coding-question-form.service';

@Component({
  selector: 'jhi-coding-question-update',
  templateUrl: './coding-question-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CodingQuestionUpdateComponent implements OnInit {
  isSaving = false;
  codingQuestion: ICodingQuestion | null = null;
  programmingLanguageValues = Object.keys(ProgrammingLanguage);

  examsSharedCollection: IExam[] = [];

  protected codingQuestionService = inject(CodingQuestionService);
  protected codingQuestionFormService = inject(CodingQuestionFormService);
  protected examService = inject(ExamService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CodingQuestionFormGroup = this.codingQuestionFormService.createCodingQuestionFormGroup();

  compareExam = (o1: IExam | null, o2: IExam | null): boolean => this.examService.compareExam(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ codingQuestion }) => {
      this.codingQuestion = codingQuestion;
      if (codingQuestion) {
        this.updateForm(codingQuestion);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const codingQuestion = this.codingQuestionFormService.getCodingQuestion(this.editForm);
    if (codingQuestion.id !== null) {
      this.subscribeToSaveResponse(this.codingQuestionService.update(codingQuestion));
    } else {
      this.subscribeToSaveResponse(this.codingQuestionService.create(codingQuestion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICodingQuestion>>): void {
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

  protected updateForm(codingQuestion: ICodingQuestion): void {
    this.codingQuestion = codingQuestion;
    this.codingQuestionFormService.resetForm(this.editForm, codingQuestion);

    this.examsSharedCollection = this.examService.addExamToCollectionIfMissing<IExam>(
      this.examsSharedCollection,
      ...(codingQuestion.exams ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.examService
      .query()
      .pipe(map((res: HttpResponse<IExam[]>) => res.body ?? []))
      .pipe(map((exams: IExam[]) => this.examService.addExamToCollectionIfMissing<IExam>(exams, ...(this.codingQuestion?.exams ?? []))))
      .subscribe((exams: IExam[]) => (this.examsSharedCollection = exams));
  }
}
