export interface IPeriod {
  id: number;
  name?: string | null;
}

export type NewPeriod = Omit<IPeriod, 'id'> & { id: null };
