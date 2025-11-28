import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPermission } from 'app/entities/permission/permission.model';
import { PermissionService } from 'app/entities/permission/service/permission.service';
import { IAuthUser } from 'app/entities/auth-user/auth-user.model';
import { AuthUserService } from 'app/entities/auth-user/service/auth-user.service';
import { IRole } from '../role.model';
import { RoleService } from '../service/role.service';
import { RoleFormService } from './role-form.service';

import { RoleUpdateComponent } from './role-update.component';

describe('Role Management Update Component', () => {
  let comp: RoleUpdateComponent;
  let fixture: ComponentFixture<RoleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let roleFormService: RoleFormService;
  let roleService: RoleService;
  let permissionService: PermissionService;
  let authUserService: AuthUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RoleUpdateComponent],
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
      .overrideTemplate(RoleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RoleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    roleFormService = TestBed.inject(RoleFormService);
    roleService = TestBed.inject(RoleService);
    permissionService = TestBed.inject(PermissionService);
    authUserService = TestBed.inject(AuthUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Permission query and add missing value', () => {
      const role: IRole = { id: 333 };
      const permissions: IPermission[] = [{ id: 19932 }];
      role.permissions = permissions;

      const permissionCollection: IPermission[] = [{ id: 19932 }];
      jest.spyOn(permissionService, 'query').mockReturnValue(of(new HttpResponse({ body: permissionCollection })));
      const additionalPermissions = [...permissions];
      const expectedCollection: IPermission[] = [...additionalPermissions, ...permissionCollection];
      jest.spyOn(permissionService, 'addPermissionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ role });
      comp.ngOnInit();

      expect(permissionService.query).toHaveBeenCalled();
      expect(permissionService.addPermissionToCollectionIfMissing).toHaveBeenCalledWith(
        permissionCollection,
        ...additionalPermissions.map(expect.objectContaining),
      );
      expect(comp.permissionsSharedCollection).toEqual(expectedCollection);
    });

    it('should call AuthUser query and add missing value', () => {
      const role: IRole = { id: 333 };
      const users: IAuthUser[] = [{ id: 4445 }];
      role.users = users;

      const authUserCollection: IAuthUser[] = [{ id: 4445 }];
      jest.spyOn(authUserService, 'query').mockReturnValue(of(new HttpResponse({ body: authUserCollection })));
      const additionalAuthUsers = [...users];
      const expectedCollection: IAuthUser[] = [...additionalAuthUsers, ...authUserCollection];
      jest.spyOn(authUserService, 'addAuthUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ role });
      comp.ngOnInit();

      expect(authUserService.query).toHaveBeenCalled();
      expect(authUserService.addAuthUserToCollectionIfMissing).toHaveBeenCalledWith(
        authUserCollection,
        ...additionalAuthUsers.map(expect.objectContaining),
      );
      expect(comp.authUsersSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const role: IRole = { id: 333 };
      const permissions: IPermission = { id: 19932 };
      role.permissions = [permissions];
      const users: IAuthUser = { id: 4445 };
      role.users = [users];

      activatedRoute.data = of({ role });
      comp.ngOnInit();

      expect(comp.permissionsSharedCollection).toContainEqual(permissions);
      expect(comp.authUsersSharedCollection).toContainEqual(users);
      expect(comp.role).toEqual(role);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRole>>();
      const role = { id: 12873 };
      jest.spyOn(roleFormService, 'getRole').mockReturnValue(role);
      jest.spyOn(roleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ role });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: role }));
      saveSubject.complete();

      // THEN
      expect(roleFormService.getRole).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(roleService.update).toHaveBeenCalledWith(expect.objectContaining(role));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRole>>();
      const role = { id: 12873 };
      jest.spyOn(roleFormService, 'getRole').mockReturnValue({ id: null });
      jest.spyOn(roleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ role: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: role }));
      saveSubject.complete();

      // THEN
      expect(roleFormService.getRole).toHaveBeenCalled();
      expect(roleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRole>>();
      const role = { id: 12873 };
      jest.spyOn(roleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ role });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(roleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePermission', () => {
      it('should forward to permissionService', () => {
        const entity = { id: 19932 };
        const entity2 = { id: 17103 };
        jest.spyOn(permissionService, 'comparePermission');
        comp.comparePermission(entity, entity2);
        expect(permissionService.comparePermission).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
