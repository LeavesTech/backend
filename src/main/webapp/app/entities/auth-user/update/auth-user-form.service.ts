import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAuthUser, NewAuthUser } from '../auth-user.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAuthUser for edit and NewAuthUserFormGroupInput for create.
 */
type AuthUserFormGroupInput = IAuthUser | PartialWithRequiredKeyOf<NewAuthUser>;

type AuthUserFormDefaults = Pick<NewAuthUser, 'id' | 'enabled' | 'roles'>;

type AuthUserFormGroupContent = {
  id: FormControl<IAuthUser['id'] | NewAuthUser['id']>;
  username: FormControl<IAuthUser['username']>;
  email: FormControl<IAuthUser['email']>;
  phoneNumber: FormControl<IAuthUser['phoneNumber']>;
  firstName: FormControl<IAuthUser['firstName']>;
  lastName: FormControl<IAuthUser['lastName']>;
  password: FormControl<IAuthUser['password']>;
  enabled: FormControl<IAuthUser['enabled']>;
  userType: FormControl<IAuthUser['userType']>;
  roles: FormControl<IAuthUser['roles']>;
};

export type AuthUserFormGroup = FormGroup<AuthUserFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AuthUserFormService {
  createAuthUserFormGroup(authUser: AuthUserFormGroupInput = { id: null }): AuthUserFormGroup {
    const authUserRawValue = {
      ...this.getFormDefaults(),
      ...authUser,
    };
    return new FormGroup<AuthUserFormGroupContent>({
      id: new FormControl(
        { value: authUserRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      username: new FormControl(authUserRawValue.username, {
        validators: [Validators.required],
      }),
      email: new FormControl(authUserRawValue.email, {
        validators: [Validators.required],
      }),
      phoneNumber: new FormControl(authUserRawValue.phoneNumber),
      firstName: new FormControl(authUserRawValue.firstName, {
        validators: [Validators.required],
      }),
      lastName: new FormControl(authUserRawValue.lastName, {
        validators: [Validators.required],
      }),
      password: new FormControl(authUserRawValue.password, {
        validators: [Validators.required],
      }),
      enabled: new FormControl(authUserRawValue.enabled, {
        validators: [Validators.required],
      }),
      userType: new FormControl(authUserRawValue.userType, {
        validators: [Validators.required],
      }),
      roles: new FormControl(authUserRawValue.roles ?? []),
    });
  }

  getAuthUser(form: AuthUserFormGroup): IAuthUser | NewAuthUser {
    return form.getRawValue() as IAuthUser | NewAuthUser;
  }

  resetForm(form: AuthUserFormGroup, authUser: AuthUserFormGroupInput): void {
    const authUserRawValue = { ...this.getFormDefaults(), ...authUser };
    form.reset(
      {
        ...authUserRawValue,
        id: { value: authUserRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AuthUserFormDefaults {
    return {
      id: null,
      enabled: false,
      roles: [],
    };
  }
}
