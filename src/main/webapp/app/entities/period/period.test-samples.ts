import { IPeriod, NewPeriod } from './period.model';

export const sampleWithRequiredData: IPeriod = {
  id: 17794,
};

export const sampleWithPartialData: IPeriod = {
  id: 17567,
  name: 'save pfft',
};

export const sampleWithFullData: IPeriod = {
  id: 16715,
  name: 'merry hmph robotics',
};

export const sampleWithNewData: NewPeriod = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
