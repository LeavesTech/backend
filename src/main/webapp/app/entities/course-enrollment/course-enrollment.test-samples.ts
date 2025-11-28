import { ICourseEnrollment, NewCourseEnrollment } from './course-enrollment.model';

export const sampleWithRequiredData: ICourseEnrollment = {
  id: 12437,
  status: 'COMPLETED',
};

export const sampleWithPartialData: ICourseEnrollment = {
  id: 17017,
  status: 'DROPPED',
};

export const sampleWithFullData: ICourseEnrollment = {
  id: 5277,
  status: 'COMPLETED',
};

export const sampleWithNewData: NewCourseEnrollment = {
  status: 'DROPPED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
