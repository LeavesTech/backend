import { IAuthUser } from 'app/entities/auth-user/auth-user.model';

export interface ITeacher {
  id: number;
  title?: string | null;
  officeRoom?: string | null;
  user?: Pick<IAuthUser, 'id'> | null;
}

export type NewTeacher = Omit<ITeacher, 'id'> & { id: null };
