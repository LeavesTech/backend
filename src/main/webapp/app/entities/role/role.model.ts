import { IPermission } from 'app/entities/permission/permission.model';
import { IAuthUser } from 'app/entities/auth-user/auth-user.model';

export interface IRole {
  id: number;
  name?: string | null;
  description?: string | null;
  permissions?: Pick<IPermission, 'id'>[] | null;
  users?: Pick<IAuthUser, 'id'>[] | null;
}

export type NewRole = Omit<IRole, 'id'> & { id: null };
