import dayjs from 'dayjs/esm';
import { IStudent } from 'app/entities/student/student.model';
import { ICodingQuestion } from 'app/entities/coding-question/coding-question.model';
import { IExam } from 'app/entities/exam/exam.model';
import { SubmissionStatus } from 'app/entities/enumerations/submission-status.model';

export interface ISubmission {
  id: number;
  submittedCode?: string | null;
  score?: number | null;
  status?: keyof typeof SubmissionStatus | null;
  submissionDate?: dayjs.Dayjs | null;
  student?: Pick<IStudent, 'id'> | null;
  question?: Pick<ICodingQuestion, 'id'> | null;
  exam?: Pick<IExam, 'id'> | null;
}

export type NewSubmission = Omit<ISubmission, 'id'> & { id: null };
