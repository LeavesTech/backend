import { ICodingQuestion, NewCodingQuestion } from './coding-question.model';

export const sampleWithRequiredData: ICodingQuestion = {
  id: 14871,
  title: 'ravage psst meh',
  description: 'hard-to-find revere',
  maxScore: 29469,
  starterCode: 'achieve',
  language: 'JAVA',
};

export const sampleWithPartialData: ICodingQuestion = {
  id: 20419,
  title: 'circa famously until',
  description: 'spook through',
  maxScore: 3832,
  starterCode: 'outrank',
  language: 'CPP',
};

export const sampleWithFullData: ICodingQuestion = {
  id: 25906,
  title: 'wilted oh requite',
  description: 'among',
  maxScore: 29105,
  starterCode: 'blah larva',
  language: 'CPP',
};

export const sampleWithNewData: NewCodingQuestion = {
  title: 'nutritious',
  description: 'dental trained',
  maxScore: 4860,
  starterCode: 'modulo minus but',
  language: 'JAVASCRIPT',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
