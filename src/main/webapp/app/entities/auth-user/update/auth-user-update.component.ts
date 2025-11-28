import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRole } from 'app/entities/role/role.model';
import { RoleService } from 'app/entities/role/service/role.service';
import { UserType } from 'app/entities/enumerations/user-type.model';
import { AuthUserService } from '../service/auth-user.service';
import { IAuthUser } from '../auth-user.model';
import { AuthUserFormGroup, AuthUserFormService } from './auth-user-form.service';

@Component({
  selector: 'jhi-auth-user-update',
  templateUrl: './auth-user-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AuthUserUpdateComponent implements OnInit {
  isSaving = false;
  authUser: IAuthUser | null = null;
  userTypeValues = Object.keys(UserType);

  rolesSharedCollection: IRole[] = [];

  protected authUserService = inject(AuthUserService);
  protected authUserFormService = inject(AuthUserFormService);
  protected roleService = inject(RoleService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AuthUserFormGroup = this.authUserFormService.createAuthUserFormGroup();

  compareRole = (o1: IRole | null, o2: IRole | null): boolean => this.roleService.compareRole(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ authUser }) => {
      this.authUser = authUser;
      if (authUser) {
        this.updateForm(authUser);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const authUser = this.authUserFormService.getAuthUser(this.editForm);
    if (authUser.id !== null) {
      this.subscribeToSaveResponse(this.authUserService.update(authUser));
    } else {
      this.subscribeToSaveResponse(this.authUserService.create(authUser));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAuthUser>>): void {
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

  protected updateForm(authUser: IAuthUser): void {
    this.authUser = authUser;
    this.authUserFormService.resetForm(this.editForm, authUser);

    this.rolesSharedCollection = this.roleService.addRoleToCollectionIfMissing<IRole>(
      this.rolesSharedCollection,
      ...(authUser.roles ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.roleService
      .query()
      .pipe(map((res: HttpResponse<IRole[]>) => res.body ?? []))
      .pipe(map((roles: IRole[]) => this.roleService.addRoleToCollectionIfMissing<IRole>(roles, ...(this.authUser?.roles ?? []))))
      .subscribe((roles: IRole[]) => (this.rolesSharedCollection = roles));
  }
}
