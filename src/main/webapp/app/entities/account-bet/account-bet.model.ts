import { IBookmaker } from 'app/entities/bookmaker/bookmaker.model';
import { AccountBetState } from 'app/entities/enumerations/account-bet-state.model';
import { AccountBetType } from 'app/entities/enumerations/account-bet-type.model';
import { BettingRoleType } from 'app/entities/enumerations/betting-role-type.model';

export interface IAccountBet {
  id: number;
  name?: string | null;
  state?: keyof typeof AccountBetState | null;
  type?: keyof typeof AccountBetType | null;
  bettingRoleType?: keyof typeof BettingRoleType | null;
  bettingRoleAmount?: number | null;
  hourActiveActive?: number | null;
  hourActiveEnd?: number | null;
  flgActiveLun?: boolean | null;
  flgActiveMar?: boolean | null;
  flgActiveMer?: boolean | null;
  flgActiveGio?: boolean | null;
  flgActiveVen?: boolean | null;
  flgActiveSab?: boolean | null;
  flgActiveDom?: boolean | null;
  whatsAppNumber?: string | null;
  userAccount?: string | null;
  passwordAccount?: string | null;
  bookmaker?: IBookmaker | null;
}

export type NewAccountBet = Omit<IAccountBet, 'id'> & { id: null };
