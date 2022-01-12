import { Page } from "@/interfaces/common/page";
import { Links } from "@/interfaces/common/links";
import { ProductResponse } from "@/interfaces/product";
import { Delivery } from "@/interfaces/common/address";
import { MemberModelResponse } from "@/interfaces/member";

export interface OrderSearchCondition {
  email: string;
  memberId: number;
  orderStatus: string;
}

export interface OrderRequest {
  memberId: number;
  productId: number;
  quantity: number;
}

export interface OrderDeleteRequest {
  memberId: number;
  orderId: number;
}

export interface OrderResponse {
  id: number;
  memberResponse: MemberModelResponse;
  orderStatus: string;
  orderProducts: OrderProductResponse[];
  delivery: Delivery;
}

export interface OrderProductResponse {
  id: number;
  product: ProductResponse;
  quantity: number;
  totalPrice: number;
  createdDateTime: string;
  lastModifiedDateTime: string;
}

export interface OrderQueryResponse {
  id: number;
  quantity: number;
  totalPrice: number;
  createdDateTime: string;
  lastModifiedDateTime: string;
  orderProducts: OrderProductResponse[];
}

export interface OrderResponsePage {
  _embedded: {
    orderQueryResponseList: OrderQueryResponse[];
  };
  _links: Links;
  page: Page;
}
