import { IExam } from 'app/entities/exam/exam.model';
import { ProgrammingLanguage } from 'app/entities/enumerations/programming-language.model';

export interface ICodingQuestion {
  id: number;
  title?: string | null;
  description?: string | null;
  maxScore?: number | null;
  starterCode?: string | null;
  language?: keyof typeof ProgrammingLanguage | null;
  exams?: Pick<IExam, 'id'>[] | null;
}

export type NewCodingQuestion = Omit<ICodingQuestion, 'id'> & { id: null };
