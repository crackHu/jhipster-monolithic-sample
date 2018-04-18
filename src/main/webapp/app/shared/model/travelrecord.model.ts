export interface ITravelrecord {
  id?: number;
  name?: string;
  phone?: number;
}

export class Travelrecord implements ITravelrecord {
  constructor(public id?: number, public name?: string, public phone?: number) {}
}
