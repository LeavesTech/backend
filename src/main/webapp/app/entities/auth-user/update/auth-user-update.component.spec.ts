import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IRole } from 'app/entities/role/role.model';
import { RoleService } from 'app/entities/role/service/role.service';
import { AuthUserService } from '../service/auth-user.service';
import { IAuthUser } from '../auth-user.model';
import { AuthUserFormService } from './auth-user-form.service';

import { AuthUserUpdateComponent } from './auth-user-update.component';

describe('AuthUser Management Update Component', () => {
  let comp: AuthUserUpdateComponent;
  let fixture: ComponentFixture<AuthUserUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let authUserFormService: AuthUserFormService;
  let authUserService: AuthUserService;
  let roleService: RoleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AuthUserUpdateComponent],
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
      .overrideTemplate(AuthUserUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AuthUserUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    authUserFormService = TestBed.inject(AuthUserFormService);
    authUserService = TestBed.inject(AuthUserService);
    roleService = TestBed.inject(RoleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Role query and add missing value', () => {
      const authUser: IAuthUser = { id: 3303 };
      const roles: IRole[] = [{ id: 12873 }];
      authUser.roles = roles;

      const roleCollection: IRole[] = [{ id: 12873 }];
      jest.spyOn(roleService, 'query').mockReturnValue(of(new HttpResponse({ body: roleCollection })));
      const additionalRoles = [...roles];
      const expectedCollection: IRole[] = [...additionalRoles, ...roleCollection];
      jest.spyOn(roleService, 'addRoleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ authUser });
      comp.ngOnInit();

      expect(roleService.query).toHaveBeenCalled();
      expect(roleService.addRoleToCollectionIfMissing).toHaveBeenCalledWith(
        roleCollection,
        ...additionalRoles.map(expect.objectContaining),
      );
      expect(comp.rolesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const authUser: IAuthUser = { id: 3303 };
      const roles: IRole = { id: 12873 };
      authUser.roles = [roles];

      activatedRoute.data = of({ authUser });
      comp.ngOnInit();

      expect(comp.rolesSharedCollection).toContainEqual(roles);
      expect(comp.authUser).toEqual(authUser);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAuthUser>>();
      const authUser = { id: 4445 };
      jest.spyOn(authUserFormService, 'getAuthUser').mockReturnValue(authUser);
      jest.spyOn(authUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ authUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: authUser }));
      saveSubject.complete();

      // THEN
      expect(authUserFormService.getAuthUser).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(authUserService.update).toHaveBeenCalledWith(expect.objectContaining(authUser));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAuthUser>>();
      const authUser = { id: 4445 };
      jest.spyOn(authUserFormService, 'getAuthUser').mockReturnValue({ id: null });
      jest.spyOn(authUserService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ authUser: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: authUser }));
      saveSubject.complete();

      // THEN
      expect(authUserFormService.getAuthUser).toHaveBeenCalled();
      expect(authUserService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAuthUser>>();
      const authUser = { id: 4445 };
      jest.spyOn(authUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ authUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(authUserService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareRole', () => {
      it('should forward to roleService', () => {
        const entity = { id: 12873 };
        const entity2 = { id: 333 };
        jest.spyOn(roleService, 'compareRole');
        comp.compareRole(entity, entity2);
        expect(roleService.compareRole).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
