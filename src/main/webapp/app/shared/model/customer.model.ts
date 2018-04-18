export interface ICustomer {
  id?: number;
  shardingId?: number;
}

export class Customer implements ICustomer {
  constructor(public id?: number, public shardingId?: number) {}
}
