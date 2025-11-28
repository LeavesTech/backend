import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRole } from 'app/entities/role/role.model';
import { RoleService } from 'app/entities/role/service/role.service';
import { PermissionCategory } from 'app/entities/enumerations/permission-category.model';
import { PermissionService } from '../service/permission.service';
import { IPermission } from '../permission.model';
import { PermissionFormGroup, PermissionFormService } from './permission-form.service';

@Component({
  selector: 'jhi-permission-update',
  templateUrl: './permission-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PermissionUpdateComponent implements OnInit {
  isSaving = false;
  permission: IPermission | null = null;
  permissionCategoryValues = Object.keys(PermissionCategory);

  rolesSharedCollection: IRole[] = [];

  protected permissionService = inject(PermissionService);
  protected permissionFormService = inject(PermissionFormService);
  protected roleService = inject(RoleService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PermissionFormGroup = this.permissionFormService.createPermissionFormGroup();

  compareRole = (o1: IRole | null, o2: IRole | null): boolean => this.roleService.compareRole(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ permission }) => {
      this.permission = permission;
      if (permission) {
        this.updateForm(permission);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const permission = this.permissionFormService.getPermission(this.editForm);
    if (permission.id !== null) {
      this.subscribeToSaveResponse(this.permissionService.update(permission));
    } else {
      this.subscribeToSaveResponse(this.permissionService.create(permission));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPermission>>): void {
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

  protected updateForm(permission: IPermission): void {
    this.permission = permission;
    this.permissionFormService.resetForm(this.editForm, permission);

    this.rolesSharedCollection = this.roleService.addRoleToCollectionIfMissing<IRole>(
      this.rolesSharedCollection,
      ...(permission.roles ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.roleService
      .query()
      .pipe(map((res: HttpResponse<IRole[]>) => res.body ?? []))
      .pipe(map((roles: IRole[]) => this.roleService.addRoleToCollectionIfMissing<IRole>(roles, ...(this.permission?.roles ?? []))))
      .subscribe((roles: IRole[]) => (this.rolesSharedCollection = roles));
  }
}
