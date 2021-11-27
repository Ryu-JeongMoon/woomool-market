import { Page } from "@/interfaces/common/page";
import { Links } from "@/interfaces/common/links";
import { ProductResponse } from "@/interfaces/product";
import { Delivery } from "@/interfaces/common/address";
import { MemberModelResponse } from "@/interfaces/member";

export interface OrderRequest {
  memberId: number;
  productId: number;
  quantity: number;
}

export interface OrderResponse {
  id: number;
  memberResponse: MemberModelResponse;
  orderStatus: string;
  orderProducts: OrderProductResponse[];
  delivery: Delivery;
}

export interface OrderResponseList {
  _embedded: {
    orderResponseList: OrderResponse[];
  };
  _links: Links;
  page: Page;
}

export interface OrderProductResponse {
  id: number;
  product: ProductResponse;
  quantity: number;
  totalPrice: number;
  createdDateTime: string;
  lastModifiedDateTime: string;
}
