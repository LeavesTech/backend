import dayjs from 'dayjs/esm';

import { ISubmission, NewSubmission } from './submission.model';

export const sampleWithRequiredData: ISubmission = {
  id: 30640,
  submittedCode: 'reluctantly',
  status: 'PASSED',
  submissionDate: dayjs('2025-11-27T21:20'),
};

export const sampleWithPartialData: ISubmission = {
  id: 22206,
  submittedCode: 'and unfinished porter',
  score: 2680.18,
  status: 'FAILED',
  submissionDate: dayjs('2025-11-27T19:36'),
};

export const sampleWithFullData: ISubmission = {
  id: 21298,
  submittedCode: 'minister brr sweetly',
  score: 32711.63,
  status: 'PASSED',
  submissionDate: dayjs('2025-11-28T07:52'),
};

export const sampleWithNewData: NewSubmission = {
  submittedCode: 'which celebrate',
  status: 'FAILED',
  submissionDate: dayjs('2025-11-28T08:40'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
