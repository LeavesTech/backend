import { IPermission, NewPermission } from './permission.model';

export const sampleWithRequiredData: IPermission = {
  id: 8197,
  key: 'forenenst pessimistic impressionable',
};

export const sampleWithPartialData: IPermission = {
  id: 4335,
  key: 'keenly healthily circle',
  category: 'COURSE_MANAGEMENT',
};

export const sampleWithFullData: IPermission = {
  id: 11619,
  key: 'beneath yum loftily',
  description: 'aside wiggly into',
  category: 'QUESTION_BANK_MANAGEMENT',
};

export const sampleWithNewData: NewPermission = {
  key: 'limply meanwhile drat',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
