import { MemberResponse } from "@/interfaces/member/member";
import { ProductResponse } from "@/interfaces/product/product";
import { CartResponseLink, Link } from "@/interfaces/common/link";
import { Page } from "@/interfaces/common/page";

export interface CartRequest {
  memberId: number;
  productId: number;
  quantity: number;
}

export interface CartResponse {
  id: number;
  quantity: number;
  memberResponse: MemberResponse;
  productResponse: ProductResponse;
  _links: CartResponseLink;
}

export interface CartResponseList {
  _embedded: {
    cartResponseList: CartResponse[];
  };
  _links: Link;
  page: Page;
}
