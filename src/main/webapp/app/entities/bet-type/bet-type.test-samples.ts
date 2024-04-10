import { IBetType, NewBetType } from './bet-type.model';

export const sampleWithRequiredData: IBetType = {
  id: 27311,
};

export const sampleWithPartialData: IBetType = {
  id: 20130,
  name: 'nickname bulge sans',
};

export const sampleWithFullData: IBetType = {
  id: 29402,
  name: 'crossly',
};

export const sampleWithNewData: NewBetType = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
