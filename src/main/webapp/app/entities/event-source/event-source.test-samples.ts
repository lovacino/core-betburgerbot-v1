import dayjs from 'dayjs/esm';

import { IEventSource, NewEventSource } from './event-source.model';

export const sampleWithRequiredData: IEventSource = {
  id: 6249,
};

export const sampleWithPartialData: IEventSource = {
  id: 8134,
  home: 'tune anti',
  betTypeParam: 27220.15,
  startedAt: dayjs('2024-04-10T05:32'),
  koef: 13434.59,
};

export const sampleWithFullData: IEventSource = {
  id: 32701,
  home: 'drat restfully',
  away: 'frail overpower apologise',
  league: 'quaintly',
  eventName: 'yowza that nice',
  bookmakerEventId: 25043,
  betTypeParam: 19298.82,
  koefLastModifiedAt: dayjs('2024-04-10T04:01'),
  scannedAt: dayjs('2024-04-10T03:02'),
  startedAt: dayjs('2024-04-10T00:48'),
  updatedAt: dayjs('2024-04-10T13:02'),
  betBurgerId: 'barring',
  koef: 25660.85,
};

export const sampleWithNewData: NewEventSource = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
