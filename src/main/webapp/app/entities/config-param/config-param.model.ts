export interface IConfigParam {
  id: number;
  paramName?: string | null;
  paramValue?: string | null;
}

export type NewConfigParam = Omit<IConfigParam, 'id'> & { id: null };
