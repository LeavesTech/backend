import { IAuthUser } from 'app/entities/auth-user/auth-user.model';
import { IDepartment } from 'app/entities/department/department.model';

export interface IStudent {
  id: number;
  studentNumber?: string | null;
  user?: Pick<IAuthUser, 'id'> | null;
  department?: Pick<IDepartment, 'id'> | null;
}

export type NewStudent = Omit<IStudent, 'id'> & { id: null };
