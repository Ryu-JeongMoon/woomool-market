import { Address } from "@/interfaces/common/address";
import { Page } from "@/interfaces/common/page";
import { Link } from "@/interfaces/common/link";

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

export interface MemberResponse {
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
}

export interface MemberResponseList {
  _embedded: {
    memberResponseList: MemberResponse[];
  };
  _links: Link;
  page: Page;
}
