export interface IOrders {
  id?: number;
  customerId?: number;
}

export class Orders implements IOrders {
  constructor(public id?: number, public customerId?: number) {}
}
