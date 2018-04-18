export interface IGoods {
  id?: number;
  name?: string;
}

export class Goods implements IGoods {
  constructor(public id?: number, public name?: string) {}
}
