import { MemberResponse } from "@/interfaces/member/member";
import { Link, ProductResponseLink } from "@/interfaces/common/link";
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
  memberResponse: MemberResponse;
  price: number;
  stock: number;
  createdDateTime: string;
  productCategory: string;
  region: string;
  _links: ProductResponseLink;
}

export interface ProductResponseList {
  _embedded: {
    productResponseList: ProductResponse[];
  };
  _links: Link;
  page: Page;
}
