export interface IOrderItems {
  id?: number;
  orderId?: number;
}

export class OrderItems implements IOrderItems {
  constructor(public id?: number, public orderId?: number) {}
}
