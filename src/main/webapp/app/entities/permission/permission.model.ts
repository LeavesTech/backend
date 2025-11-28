import { IRole } from 'app/entities/role/role.model';
import { PermissionCategory } from 'app/entities/enumerations/permission-category.model';

export interface IPermission {
  id: number;
  key?: string | null;
  description?: string | null;
  category?: keyof typeof PermissionCategory | null;
  roles?: Pick<IRole, 'id'>[] | null;
}

export type NewPermission = Omit<IPermission, 'id'> & { id: null };
