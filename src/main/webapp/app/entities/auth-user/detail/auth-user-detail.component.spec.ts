import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AuthUserDetailComponent } from './auth-user-detail.component';

describe('AuthUser Management Detail Component', () => {
  let comp: AuthUserDetailComponent;
  let fixture: ComponentFixture<AuthUserDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AuthUserDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./auth-user-detail.component').then(m => m.AuthUserDetailComponent),
              resolve: { authUser: () => of({ id: 4445 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AuthUserDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AuthUserDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load authUser on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AuthUserDetailComponent);

      // THEN
      expect(instance.authUser()).toEqual(expect.objectContaining({ id: 4445 }));
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
