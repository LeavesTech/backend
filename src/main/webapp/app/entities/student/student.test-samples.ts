import { IStudent, NewStudent } from './student.model';

export const sampleWithRequiredData: IStudent = {
  id: 15380,
  studentNumber: 'new instead married',
};

export const sampleWithPartialData: IStudent = {
  id: 12091,
  studentNumber: 'aside hmph tomography',
};

export const sampleWithFullData: IStudent = {
  id: 6617,
  studentNumber: 'pleasant plain petty',
};

export const sampleWithNewData: NewStudent = {
  studentNumber: 'gad even aboard',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
