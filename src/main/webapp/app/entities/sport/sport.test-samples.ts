import { ISport, NewSport } from './sport.model';

export const sampleWithRequiredData: ISport = {
  id: 6156,
};

export const sampleWithPartialData: ISport = {
  id: 13204,
};

export const sampleWithFullData: ISport = {
  id: 14391,
  name: 'spout',
};

export const sampleWithNewData: NewSport = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
