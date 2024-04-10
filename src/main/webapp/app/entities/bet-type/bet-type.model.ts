export interface IBetType {
  id: number;
  name?: string | null;
}

export type NewBetType = Omit<IBetType, 'id'> & { id: null };
