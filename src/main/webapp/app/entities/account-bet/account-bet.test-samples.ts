import { IAccountBet, NewAccountBet } from './account-bet.model';

export const sampleWithRequiredData: IAccountBet = {
  id: 28168,
};

export const sampleWithPartialData: IAccountBet = {
  id: 502,
  name: 'impassioned',
  state: 'ACTIVE',
  type: 'WHATSAPP',
  hourActiveStart: 18202,
  flgActiveLun: true,
  flgActiveMar: true,
  flgActiveSab: false,
  flgActiveDom: false,
  whatsAppNumber: 'even eek on',
  userAccount: 'aerate at heavily',
};

export const sampleWithFullData: IAccountBet = {
  id: 31981,
  name: 'although considering',
  state: 'CLOSED',
  type: 'ONLINEBOT',
  bettingRoleType: 'FIXED_BET',
  bettingRoleAmount: 23387.71,
  hourActiveStart: 23549,
  hourActiveEnd: 3132,
  flgActiveLun: true,
  flgActiveMar: true,
  flgActiveMer: false,
  flgActiveGio: true,
  flgActiveVen: true,
  flgActiveSab: true,
  flgActiveDom: true,
  whatsAppNumber: 'fascia',
  userAccount: 'incidentally',
  passwordAccount: 'wisecrack zowie trunk',
};

export const sampleWithNewData: NewAccountBet = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
