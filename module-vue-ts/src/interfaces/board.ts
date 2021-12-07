import { MemberModelResponse } from "@/interfaces/member";
import { BoardResponseLinks, Links } from "@/interfaces/common/links";
import { Page } from "@/interfaces/common/page";

export interface BoardSearchCondition {
  email: string;
  title: string;
  content: string;
  status: string;
  boardCategory: string;
}

// 형식 수정 필요
export interface BoardRequest {
  email: string;
  title: string;
  content: string;
  boardCategory: string;
  startDateTime: string;
  endDateTime: string;
}

export interface BoardModifyRequest {
  title: string;
  content: string;
  boardCategory: string;
}

export interface BoardResponse {
  id: number;
  title: string;
  content: string;
  hit: number;
  boardCategory: string;
  startDateTime: string;
  endDateTime: string;
  createdDateTime: string;
  memberResponse: MemberModelResponse;
  _links: BoardResponseLinks;
}

export interface BoardResponseList {
  _embedded: {
    boardQueryResponseList: BoardResponse[];
  };
  _links: Links;
  page: Page;
}
