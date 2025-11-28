import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAuthUser } from 'app/entities/auth-user/auth-user.model';
import { AuthUserService } from 'app/entities/auth-user/service/auth-user.service';
import { ITeacher } from '../teacher.model';
import { TeacherService } from '../service/teacher.service';
import { TeacherFormGroup, TeacherFormService } from './teacher-form.service';

@Component({
  selector: 'jhi-teacher-update',
  templateUrl: './teacher-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TeacherUpdateComponent implements OnInit {
  isSaving = false;
  teacher: ITeacher | null = null;

  usersCollection: IAuthUser[] = [];

  protected teacherService = inject(TeacherService);
  protected teacherFormService = inject(TeacherFormService);
  protected authUserService = inject(AuthUserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TeacherFormGroup = this.teacherFormService.createTeacherFormGroup();

  compareAuthUser = (o1: IAuthUser | null, o2: IAuthUser | null): boolean => this.authUserService.compareAuthUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ teacher }) => {
      this.teacher = teacher;
      if (teacher) {
        this.updateForm(teacher);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const teacher = this.teacherFormService.getTeacher(this.editForm);
    if (teacher.id !== null) {
      this.subscribeToSaveResponse(this.teacherService.update(teacher));
    } else {
      this.subscribeToSaveResponse(this.teacherService.create(teacher));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITeacher>>): void {
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

  protected updateForm(teacher: ITeacher): void {
    this.teacher = teacher;
    this.teacherFormService.resetForm(this.editForm, teacher);

    this.usersCollection = this.authUserService.addAuthUserToCollectionIfMissing<IAuthUser>(this.usersCollection, teacher.user);
  }

  protected loadRelationshipsOptions(): void {
    this.authUserService
      .query({ filter: 'teacher-is-null' })
      .pipe(map((res: HttpResponse<IAuthUser[]>) => res.body ?? []))
      .pipe(
        map((authUsers: IAuthUser[]) => this.authUserService.addAuthUserToCollectionIfMissing<IAuthUser>(authUsers, this.teacher?.user)),
      )
      .subscribe((authUsers: IAuthUser[]) => (this.usersCollection = authUsers));
  }
}
