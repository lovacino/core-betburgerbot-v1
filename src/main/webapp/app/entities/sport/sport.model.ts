export interface ISport {
  id: number;
  name?: string | null;
}

export type NewSport = Omit<ISport, 'id'> & { id: null };
