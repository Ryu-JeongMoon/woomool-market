import { MemberModelResponse, MemberQueryResponse } from "@/interfaces/member";
import { Links, ProductResponseLinks } from "@/interfaces/common/links";
import { Page } from "@/interfaces/common/page";

export interface ProductRequest {
  name: string;
  email: string;
  description: string;
  productImage: string;
  price: number;
  stock: number;
  region: string;
  productCategory: string;
}

export interface ProductModifyRequest {
  name: string;
  description: string;
  productImage: string;
  price: number;
  stock: number;
  region: string;
  productCategory: string;
}

export interface ProductResponse {
  name: string;
  description: string;
  productImage: string;
  memberResponse: MemberModelResponse;
  price: number;
  stock: number;
  createdDateTime: string;
  productCategory: string;
  region: string;
  _links: ProductResponseLinks;
}

export interface ProductQueryResponse {
  name: string;
  description: string;
  productImage: string;
  price: number;
  stock: number;
  region: string;
  createdDateTime: string;
  productCategory: string;
  memberQueryResponse: MemberQueryResponse;
  _links: ProductResponseLinks;
}

export interface ProductResponseList {
  _embedded: {
    productResponseList: ProductResponse[];
  };
  _links: Links;
  page: Page;
}
