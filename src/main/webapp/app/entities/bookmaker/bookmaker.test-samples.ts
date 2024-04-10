import { IBookmaker, NewBookmaker } from './bookmaker.model';

export const sampleWithRequiredData: IBookmaker = {
  id: 8249,
};

export const sampleWithPartialData: IBookmaker = {
  id: 31949,
  state: 'INACTIVE',
};

export const sampleWithFullData: IBookmaker = {
  id: 21083,
  name: 'generally',
  state: 'INACTIVE',
};

export const sampleWithNewData: NewBookmaker = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
