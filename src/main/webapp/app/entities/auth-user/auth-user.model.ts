import { IRole } from 'app/entities/role/role.model';
import { UserType } from 'app/entities/enumerations/user-type.model';

export interface IAuthUser {
  id: number;
  username?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  password?: string | null;
  enabled?: boolean | null;
  userType?: keyof typeof UserType | null;
  roles?: Pick<IRole, 'id'>[] | null;
}

export type NewAuthUser = Omit<IAuthUser, 'id'> & { id: null };
