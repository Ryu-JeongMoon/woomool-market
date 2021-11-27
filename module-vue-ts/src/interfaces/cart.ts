import { MemberModelResponse } from "@/interfaces/member";
import { ProductResponse } from "@/interfaces/product";
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

export interface CartResponseList {
  _embedded: {
    cartResponseList: CartResponse[];
  };
  _links: Links;
  page: Page;
}
