import dayjs from 'dayjs/esm';
import { IBookmaker } from 'app/entities/bookmaker/bookmaker.model';
import { ISport } from 'app/entities/sport/sport.model';
import { IPeriod } from 'app/entities/period/period.model';
import { IBetType } from 'app/entities/bet-type/bet-type.model';

export interface IEventSource {
  id: number;
  home?: string | null;
  away?: string | null;
  league?: string | null;
  eventName?: string | null;
  bookmakerEventId?: number | null;
  betTypeParam?: number | null;
  koefLastModifiedAt?: dayjs.Dayjs | null;
  scannedAt?: dayjs.Dayjs | null;
  startedAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  betBurgerId?: string | null;
  koef?: number | null;
  bookmaker?: IBookmaker | null;
  sport?: ISport | null;
  period?: IPeriod | null;
  betType?: IBetType | null;
}

export type NewEventSource = Omit<IEventSource, 'id'> & { id: null };
