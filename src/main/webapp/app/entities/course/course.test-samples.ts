import { ICourse, NewCourse } from './course.model';

export const sampleWithRequiredData: ICourse = {
  id: 8824,
  code: 'radiant',
  name: 'sturdy',
  term: 'vice',
};

export const sampleWithPartialData: ICourse = {
  id: 1689,
  code: 'while',
  name: 'ugh',
  term: 'up questioningly',
};

export const sampleWithFullData: ICourse = {
  id: 30968,
  code: 'weakly',
  name: 'whenever pro',
  term: 'floss amidst',
};

export const sampleWithNewData: NewCourse = {
  code: 'unto',
  name: 'enchanting vivaciously',
  term: 'monstrous quietly aw',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
