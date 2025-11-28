import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { CourseEnrollmentDetailComponent } from './course-enrollment-detail.component';

describe('CourseEnrollment Management Detail Component', () => {
  let comp: CourseEnrollmentDetailComponent;
  let fixture: ComponentFixture<CourseEnrollmentDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CourseEnrollmentDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./course-enrollment-detail.component').then(m => m.CourseEnrollmentDetailComponent),
              resolve: { courseEnrollment: () => of({ id: 11487 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CourseEnrollmentDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CourseEnrollmentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load courseEnrollment on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CourseEnrollmentDetailComponent);

      // THEN
      expect(instance.courseEnrollment()).toEqual(expect.objectContaining({ id: 11487 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
