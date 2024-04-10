import dayjs from 'dayjs/esm';
import { IAccountBet } from 'app/entities/account-bet/account-bet.model';
import { ISport } from 'app/entities/sport/sport.model';
import { IPeriod } from 'app/entities/period/period.model';
import { IBetType } from 'app/entities/bet-type/bet-type.model';
import { BettingState } from 'app/entities/enumerations/betting-state.model';
import { BetResultType } from 'app/entities/enumerations/bet-result-type.model';

export interface IBetting {
  id: number;
  eventSourceId?: number | null;
  betTypeParam?: number | null;
  koef?: number | null;
  home?: string | null;
  away?: string | null;
  league?: string | null;
  eventName?: string | null;
  startedAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  betBurgerId?: string | null;
  state?: keyof typeof BettingState | null;
  amountBet?: number | null;
  amountBetWin?: number | null;
  betResultType?: keyof typeof BetResultType | null;
  account?: IAccountBet | null;
  sport?: ISport | null;
  period?: IPeriod | null;
  betType?: IBetType | null;
}

export type NewBetting = Omit<IBetting, 'id'> & { id: null };
