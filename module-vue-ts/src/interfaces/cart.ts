import { MemberModelResponse, MemberQueryResponse } from "@/interfaces/member";
import { ProductQueryResponse, ProductResponse } from "@/interfaces/product";
import { CartResponseLinks, Links } from "@/interfaces/common/links";
import { Page } from "@/interfaces/common/page";

export interface CartRequest {
  memberId: number;
  productId: number;
  quantity: number;
}

export interface CartResponse {
  id: number;
  quantity: number;
  memberResponse: MemberModelResponse;
  productResponse: ProductResponse;
  _links: CartResponseLinks;
}

export interface CartQueryResponse {
  id: number;
  quantity: number;
  memberQueryResponse: MemberQueryResponse;
  productQueryResponse: ProductQueryResponse;
  _links: CartResponseLinks;
}

export interface CartResponseList {
  _embedded: {
    cartResponseList: CartResponse[];
  };
  _links: Links;
  page: Page;
}

export interface CartResponsePage {
  _embedded: {
    cartQueryResponseList: CartQueryResponse[];
  };
  _links: Links;
  page: Page;
}
