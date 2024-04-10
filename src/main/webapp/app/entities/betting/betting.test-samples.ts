import dayjs from 'dayjs/esm';

import { IBetting, NewBetting } from './betting.model';

export const sampleWithRequiredData: IBetting = {
  id: 32579,
};

export const sampleWithPartialData: IBetting = {
  id: 22270,
  eventSourceId: 3431,
  koef: 17277.64,
  startedAt: dayjs('2024-04-09T21:44'),
  betBurgerId: 'equally',
  amountBetWin: 27884.54,
};

export const sampleWithFullData: IBetting = {
  id: 28149,
  eventSourceId: 11352,
  betTypeParam: 11177.63,
  koef: 13494.71,
  home: 'indeed valiantly',
  away: 'jar',
  league: 'er continually',
  eventName: 'wrangle',
  startedAt: dayjs('2024-04-10T11:12'),
  updatedAt: dayjs('2024-04-10T00:50'),
  betBurgerId: 'gee',
  state: 'SENDED',
  amountBet: 20300.93,
  amountBetWin: 3742.46,
  betResultType: 'LOSE',
};

export const sampleWithNewData: NewBetting = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
