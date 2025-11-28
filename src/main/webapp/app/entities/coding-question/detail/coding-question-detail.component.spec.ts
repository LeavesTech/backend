import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { CodingQuestionDetailComponent } from './coding-question-detail.component';

describe('CodingQuestion Management Detail Component', () => {
  let comp: CodingQuestionDetailComponent;
  let fixture: ComponentFixture<CodingQuestionDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CodingQuestionDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./coding-question-detail.component').then(m => m.CodingQuestionDetailComponent),
              resolve: { codingQuestion: () => of({ id: 2391 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CodingQuestionDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CodingQuestionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load codingQuestion on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CodingQuestionDetailComponent);

      // THEN
      expect(instance.codingQuestion()).toEqual(expect.objectContaining({ id: 2391 }));
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
