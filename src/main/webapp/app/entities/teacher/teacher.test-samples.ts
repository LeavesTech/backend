import { ITeacher, NewTeacher } from './teacher.model';

export const sampleWithRequiredData: ITeacher = {
  id: 9510,
};

export const sampleWithPartialData: ITeacher = {
  id: 23907,
};

export const sampleWithFullData: ITeacher = {
  id: 30957,
  title: 'lest consequently',
  officeRoom: 'crank because',
};

export const sampleWithNewData: NewTeacher = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
