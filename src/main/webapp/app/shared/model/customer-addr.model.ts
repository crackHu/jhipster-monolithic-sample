export interface ICustomerAddr {
  id?: number;
  customerId?: number;
}

export class CustomerAddr implements ICustomerAddr {
  constructor(public id?: number, public customerId?: number) {}
}
