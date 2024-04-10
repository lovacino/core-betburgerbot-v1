import { BookmakerState } from 'app/entities/enumerations/bookmaker-state.model';

export interface IBookmaker {
  id: number;
  name?: string | null;
  state?: keyof typeof BookmakerState | null;
}

export type NewBookmaker = Omit<IBookmaker, 'id'> & { id: null };
