export interface IHotnews {
  id?: number;
  name?: string;
}

export class Hotnews implements IHotnews {
  constructor(public id?: number, public name?: string) {}
}
