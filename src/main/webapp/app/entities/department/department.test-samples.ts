import { IDepartment, NewDepartment } from './department.model';

export const sampleWithRequiredData: IDepartment = {
  id: 32001,
  name: 'phew',
};

export const sampleWithPartialData: IDepartment = {
  id: 18435,
  name: 'sneak phew daintily',
};

export const sampleWithFullData: IDepartment = {
  id: 13276,
  name: 'endow',
};

export const sampleWithNewData: NewDepartment = {
  name: 'peaceful if',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
