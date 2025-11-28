import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICourse } from 'app/entities/course/course.model';
import { CourseService } from 'app/entities/course/service/course.service';
import { IStudent } from 'app/entities/student/student.model';
import { StudentService } from 'app/entities/student/service/student.service';
import { EnrollmentStatus } from 'app/entities/enumerations/enrollment-status.model';
import { CourseEnrollmentService } from '../service/course-enrollment.service';
import { ICourseEnrollment } from '../course-enrollment.model';
import { CourseEnrollmentFormGroup, CourseEnrollmentFormService } from './course-enrollment-form.service';

@Component({
  selector: 'jhi-course-enrollment-update',
  templateUrl: './course-enrollment-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CourseEnrollmentUpdateComponent implements OnInit {
  isSaving = false;
  courseEnrollment: ICourseEnrollment | null = null;
  enrollmentStatusValues = Object.keys(EnrollmentStatus);

  coursesSharedCollection: ICourse[] = [];
  studentsSharedCollection: IStudent[] = [];

  protected courseEnrollmentService = inject(CourseEnrollmentService);
  protected courseEnrollmentFormService = inject(CourseEnrollmentFormService);
  protected courseService = inject(CourseService);
  protected studentService = inject(StudentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CourseEnrollmentFormGroup = this.courseEnrollmentFormService.createCourseEnrollmentFormGroup();

  compareCourse = (o1: ICourse | null, o2: ICourse | null): boolean => this.courseService.compareCourse(o1, o2);

  compareStudent = (o1: IStudent | null, o2: IStudent | null): boolean => this.studentService.compareStudent(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courseEnrollment }) => {
      this.courseEnrollment = courseEnrollment;
      if (courseEnrollment) {
        this.updateForm(courseEnrollment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const courseEnrollment = this.courseEnrollmentFormService.getCourseEnrollment(this.editForm);
    if (courseEnrollment.id !== null) {
      this.subscribeToSaveResponse(this.courseEnrollmentService.update(courseEnrollment));
    } else {
      this.subscribeToSaveResponse(this.courseEnrollmentService.create(courseEnrollment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourseEnrollment>>): void {
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

  protected updateForm(courseEnrollment: ICourseEnrollment): void {
    this.courseEnrollment = courseEnrollment;
    this.courseEnrollmentFormService.resetForm(this.editForm, courseEnrollment);

    this.coursesSharedCollection = this.courseService.addCourseToCollectionIfMissing<ICourse>(
      this.coursesSharedCollection,
      courseEnrollment.course,
    );
    this.studentsSharedCollection = this.studentService.addStudentToCollectionIfMissing<IStudent>(
      this.studentsSharedCollection,
      courseEnrollment.student,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.courseService
      .query()
      .pipe(map((res: HttpResponse<ICourse[]>) => res.body ?? []))
      .pipe(map((courses: ICourse[]) => this.courseService.addCourseToCollectionIfMissing<ICourse>(courses, this.courseEnrollment?.course)))
      .subscribe((courses: ICourse[]) => (this.coursesSharedCollection = courses));

    this.studentService
      .query()
      .pipe(map((res: HttpResponse<IStudent[]>) => res.body ?? []))
      .pipe(
        map((students: IStudent[]) =>
          this.studentService.addStudentToCollectionIfMissing<IStudent>(students, this.courseEnrollment?.student),
        ),
      )
      .subscribe((students: IStudent[]) => (this.studentsSharedCollection = students));
  }
}
