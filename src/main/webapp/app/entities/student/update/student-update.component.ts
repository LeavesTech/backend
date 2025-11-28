import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAuthUser } from 'app/entities/auth-user/auth-user.model';
import { AuthUserService } from 'app/entities/auth-user/service/auth-user.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { StudentService } from '../service/student.service';
import { IStudent } from '../student.model';
import { StudentFormGroup, StudentFormService } from './student-form.service';

@Component({
  selector: 'jhi-student-update',
  templateUrl: './student-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class StudentUpdateComponent implements OnInit {
  isSaving = false;
  student: IStudent | null = null;

  usersCollection: IAuthUser[] = [];
  departmentsSharedCollection: IDepartment[] = [];

  protected studentService = inject(StudentService);
  protected studentFormService = inject(StudentFormService);
  protected authUserService = inject(AuthUserService);
  protected departmentService = inject(DepartmentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: StudentFormGroup = this.studentFormService.createStudentFormGroup();

  compareAuthUser = (o1: IAuthUser | null, o2: IAuthUser | null): boolean => this.authUserService.compareAuthUser(o1, o2);

  compareDepartment = (o1: IDepartment | null, o2: IDepartment | null): boolean => this.departmentService.compareDepartment(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ student }) => {
      this.student = student;
      if (student) {
        this.updateForm(student);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const student = this.studentFormService.getStudent(this.editForm);
    if (student.id !== null) {
      this.subscribeToSaveResponse(this.studentService.update(student));
    } else {
      this.subscribeToSaveResponse(this.studentService.create(student));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudent>>): void {
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

  protected updateForm(student: IStudent): void {
    this.student = student;
    this.studentFormService.resetForm(this.editForm, student);

    this.usersCollection = this.authUserService.addAuthUserToCollectionIfMissing<IAuthUser>(this.usersCollection, student.user);
    this.departmentsSharedCollection = this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(
      this.departmentsSharedCollection,
      student.department,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.authUserService
      .query({ filter: 'student-is-null' })
      .pipe(map((res: HttpResponse<IAuthUser[]>) => res.body ?? []))
      .pipe(
        map((authUsers: IAuthUser[]) => this.authUserService.addAuthUserToCollectionIfMissing<IAuthUser>(authUsers, this.student?.user)),
      )
      .subscribe((authUsers: IAuthUser[]) => (this.usersCollection = authUsers));

    this.departmentService
      .query()
      .pipe(map((res: HttpResponse<IDepartment[]>) => res.body ?? []))
      .pipe(
        map((departments: IDepartment[]) =>
          this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(departments, this.student?.department),
        ),
      )
      .subscribe((departments: IDepartment[]) => (this.departmentsSharedCollection = departments));
  }
}
