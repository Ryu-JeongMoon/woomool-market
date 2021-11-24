import { Page } from "@/interfaces/common/page";
import { Link } from "@/interfaces/common/link";
import { ProductResponse } from "@/interfaces/product/product";
import { Delivery } from "@/interfaces/common/address";
import { MemberResponse } from "@/interfaces/member/member";

export interface OrderRequest {
  memberId: number;
  productId: number;
  quantity: number;
}

export interface OrderResponse {
  id: number;
  memberResponse: MemberResponse;
  orderStatus: string;
  orderProducts: OrderProductResponse[];
  delivery: Delivery;
}

export interface OrderResponseList {
  _embedded: {
    orderResponseList: OrderResponse[];
  };
  _links: Link;
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
