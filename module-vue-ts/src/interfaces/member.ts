import { Address } from "@/interfaces/common/address";
import { Page } from "@/interfaces/common/page";
import { Links } from "@/interfaces/common/links";

export interface LoginRequest {
  email: string;
  password: string;
}

export interface MemberRequest {
  email: string;
  nickname: string;
  password: string;
  address: Address;
}

export interface MemberModifyRequest {
  nickname: string;
  password: string;
  profileImage: string;
  phone: string;
  license: string;
  address: Address;
}

export interface MemberModelResponse {
  id: number;
  email: string;
  nickname: string;
  profileImage: string;
  phone: string;
  license: string;
  createdDateTime: string;
  lastModifiedDateTime: string;
  leaveDateTime: string;
  authority: string;
  address: Address;
  authProvider: string;
  status: string;
  _links: Links;
}

export interface PagedMemberResponse {
  _embedded: {
    memberResponseList: MemberModelResponse[];
  };
  _links: Links;
  page: Page;
}
