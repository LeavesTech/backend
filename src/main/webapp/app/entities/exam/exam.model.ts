import dayjs from 'dayjs/esm';
import { ICourse } from 'app/entities/course/course.model';
import { ICodingQuestion } from 'app/entities/coding-question/coding-question.model';
import { ExamType } from 'app/entities/enumerations/exam-type.model';

export interface IExam {
  id: number;
  title?: string | null;
  startedAt?: dayjs.Dayjs | null;
  endAt?: dayjs.Dayjs | null;
  durationTime?: number | null;
  type?: keyof typeof ExamType | null;
  course?: Pick<ICourse, 'id'> | null;
  questions?: Pick<ICodingQuestion, 'id'>[] | null;
}

export type NewExam = Omit<IExam, 'id'> & { id: null };
