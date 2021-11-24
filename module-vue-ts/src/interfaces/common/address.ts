export interface Address {
  city: string;
  street: string;
  zipcode: string;
}

export interface Delivery {
  receiver: string;
  address: Address;
  phone: string;
}
