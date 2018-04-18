export interface IEmployee {
  id?: number;
  name?: string;
  shardingId?: number;
}

export class Employee implements IEmployee {
  constructor(public id?: number, public name?: string, public shardingId?: number) {}
}
