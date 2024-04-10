import { IConfigParam, NewConfigParam } from './config-param.model';

export const sampleWithRequiredData: IConfigParam = {
  id: 31010,
};

export const sampleWithPartialData: IConfigParam = {
  id: 30770,
  paramValue: 'poorly supplement regular',
};

export const sampleWithFullData: IConfigParam = {
  id: 25914,
  paramName: 'opposite toward leg',
  paramValue: 'phooey meh even',
};

export const sampleWithNewData: NewConfigParam = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
