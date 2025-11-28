import { ICourse } from 'app/entities/course/course.model';
import { IStudent } from 'app/entities/student/student.model';
import { EnrollmentStatus } from 'app/entities/enumerations/enrollment-status.model';

export interface ICourseEnrollment {
  id: number;
  status?: keyof typeof EnrollmentStatus | null;
  course?: Pick<ICourse, 'id'> | null;
  student?: Pick<IStudent, 'id'> | null;
}

export type NewCourseEnrollment = Omit<ICourseEnrollment, 'id'> & { id: null };
