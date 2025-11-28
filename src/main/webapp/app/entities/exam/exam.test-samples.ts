import dayjs from 'dayjs/esm';

import { IExam, NewExam } from './exam.model';

export const sampleWithRequiredData: IExam = {
  id: 19529,
  title: 'quizzically viciously unlike',
  startedAt: dayjs('2025-11-28T08:55'),
  endAt: dayjs('2025-11-28T09:45'),
  durationTime: 1161,
  type: 'SUPPLEMENTARY',
};

export const sampleWithPartialData: IExam = {
  id: 8408,
  title: 'downchange',
  startedAt: dayjs('2025-11-27T16:05'),
  endAt: dayjs('2025-11-27T16:43'),
  durationTime: 11492,
  type: 'SUPPLEMENTARY',
};

export const sampleWithFullData: IExam = {
  id: 21245,
  title: 'critical upright',
  startedAt: dayjs('2025-11-27T22:12'),
  endAt: dayjs('2025-11-28T04:29'),
  durationTime: 14461,
  type: 'MIDTERM',
};

export const sampleWithNewData: NewExam = {
  title: 'culture ack',
  startedAt: dayjs('2025-11-28T13:36'),
  endAt: dayjs('2025-11-27T20:30'),
  durationTime: 20276,
  type: 'SUPPLEMENTARY',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
