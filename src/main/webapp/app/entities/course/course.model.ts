import { ITeacher } from 'app/entities/teacher/teacher.model';
import { IDepartment } from 'app/entities/department/department.model';

export interface ICourse {
  id: number;
  code?: string | null;
  name?: string | null;
  term?: string | null;
  owner?: Pick<ITeacher, 'id'> | null;
  department?: Pick<IDepartment, 'id'> | null;
}

export type NewCourse = Omit<ICourse, 'id'> & { id: null };
