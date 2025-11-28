import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ICourseEnrollment } from '../course-enrollment.model';

@Component({
  selector: 'jhi-course-enrollment-detail',
  templateUrl: './course-enrollment-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class CourseEnrollmentDetailComponent {
  courseEnrollment = input<ICourseEnrollment | null>(null);

  previousState(): void {
    window.history.back();
  }
}
