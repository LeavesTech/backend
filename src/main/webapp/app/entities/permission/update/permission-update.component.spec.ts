import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IRole } from 'app/entities/role/role.model';
import { RoleService } from 'app/entities/role/service/role.service';
import { PermissionService } from '../service/permission.service';
import { IPermission } from '../permission.model';
import { PermissionFormService } from './permission-form.service';

import { PermissionUpdateComponent } from './permission-update.component';

describe('Permission Management Update Component', () => {
  let comp: PermissionUpdateComponent;
  let fixture: ComponentFixture<PermissionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let permissionFormService: PermissionFormService;
  let permissionService: PermissionService;
  let roleService: RoleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PermissionUpdateComponent],
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
      .overrideTemplate(PermissionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PermissionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    permissionFormService = TestBed.inject(PermissionFormService);
    permissionService = TestBed.inject(PermissionService);
    roleService = TestBed.inject(RoleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Role query and add missing value', () => {
      const permission: IPermission = { id: 17103 };
      const roles: IRole[] = [{ id: 12873 }];
      permission.roles = roles;

      const roleCollection: IRole[] = [{ id: 12873 }];
      jest.spyOn(roleService, 'query').mockReturnValue(of(new HttpResponse({ body: roleCollection })));
      const additionalRoles = [...roles];
      const expectedCollection: IRole[] = [...additionalRoles, ...roleCollection];
      jest.spyOn(roleService, 'addRoleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ permission });
      comp.ngOnInit();

      expect(roleService.query).toHaveBeenCalled();
      expect(roleService.addRoleToCollectionIfMissing).toHaveBeenCalledWith(
        roleCollection,
        ...additionalRoles.map(expect.objectContaining),
      );
      expect(comp.rolesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const permission: IPermission = { id: 17103 };
      const roles: IRole = { id: 12873 };
      permission.roles = [roles];

      activatedRoute.data = of({ permission });
      comp.ngOnInit();

      expect(comp.rolesSharedCollection).toContainEqual(roles);
      expect(comp.permission).toEqual(permission);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPermission>>();
      const permission = { id: 19932 };
      jest.spyOn(permissionFormService, 'getPermission').mockReturnValue(permission);
      jest.spyOn(permissionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ permission });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: permission }));
      saveSubject.complete();

      // THEN
      expect(permissionFormService.getPermission).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(permissionService.update).toHaveBeenCalledWith(expect.objectContaining(permission));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPermission>>();
      const permission = { id: 19932 };
      jest.spyOn(permissionFormService, 'getPermission').mockReturnValue({ id: null });
      jest.spyOn(permissionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ permission: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: permission }));
      saveSubject.complete();

      // THEN
      expect(permissionFormService.getPermission).toHaveBeenCalled();
      expect(permissionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPermission>>();
      const permission = { id: 19932 };
      jest.spyOn(permissionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ permission });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(permissionService.update).toHaveBeenCalled();
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
