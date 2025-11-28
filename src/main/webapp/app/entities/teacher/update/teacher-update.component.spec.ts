import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IAuthUser } from 'app/entities/auth-user/auth-user.model';
import { AuthUserService } from 'app/entities/auth-user/service/auth-user.service';
import { TeacherService } from '../service/teacher.service';
import { ITeacher } from '../teacher.model';
import { TeacherFormService } from './teacher-form.service';

import { TeacherUpdateComponent } from './teacher-update.component';

describe('Teacher Management Update Component', () => {
  let comp: TeacherUpdateComponent;
  let fixture: ComponentFixture<TeacherUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let teacherFormService: TeacherFormService;
  let teacherService: TeacherService;
  let authUserService: AuthUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TeacherUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TeacherUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TeacherUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    teacherFormService = TestBed.inject(TeacherFormService);
    teacherService = TestBed.inject(TeacherService);
    authUserService = TestBed.inject(AuthUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call user query and add missing value', () => {
      const teacher: ITeacher = { id: 13207 };
      const user: IAuthUser = { id: 4445 };
      teacher.user = user;

      const userCollection: IAuthUser[] = [{ id: 4445 }];
      jest.spyOn(authUserService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const expectedCollection: IAuthUser[] = [user, ...userCollection];
      jest.spyOn(authUserService, 'addAuthUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ teacher });
      comp.ngOnInit();

      expect(authUserService.query).toHaveBeenCalled();
      expect(authUserService.addAuthUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, user);
      expect(comp.usersCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const teacher: ITeacher = { id: 13207 };
      const user: IAuthUser = { id: 4445 };
      teacher.user = user;

      activatedRoute.data = of({ teacher });
      comp.ngOnInit();

      expect(comp.usersCollection).toContainEqual(user);
      expect(comp.teacher).toEqual(teacher);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITeacher>>();
      const teacher = { id: 11312 };
      jest.spyOn(teacherFormService, 'getTeacher').mockReturnValue(teacher);
      jest.spyOn(teacherService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ teacher });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: teacher }));
      saveSubject.complete();

      // THEN
      expect(teacherFormService.getTeacher).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(teacherService.update).toHaveBeenCalledWith(expect.objectContaining(teacher));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITeacher>>();
      const teacher = { id: 11312 };
      jest.spyOn(teacherFormService, 'getTeacher').mockReturnValue({ id: null });
      jest.spyOn(teacherService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ teacher: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: teacher }));
      saveSubject.complete();

      // THEN
      expect(teacherFormService.getTeacher).toHaveBeenCalled();
      expect(teacherService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITeacher>>();
      const teacher = { id: 11312 };
      jest.spyOn(teacherService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ teacher });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(teacherService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAuthUser', () => {
      it('should forward to authUserService', () => {
        const entity = { id: 4445 };
        const entity2 = { id: 3303 };
        jest.spyOn(authUserService, 'compareAuthUser');
        comp.compareAuthUser(entity, entity2);
        expect(authUserService.compareAuthUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
