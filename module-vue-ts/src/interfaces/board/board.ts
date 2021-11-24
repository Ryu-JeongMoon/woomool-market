import { MemberResponse } from "@/interfaces/member/member";
import { BoardResponseLink, Link } from "@/interfaces/common/link";
import { Page } from "@/interfaces/common/page";

export interface BoardSearchCondition {
  email?: string;
  title?: string;
  content?: string;
  status?: string;
  boardCategory?: string;
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

export interface BoardResponse {
  id: number;
  title: string;
  content: string;
  hit: number;
  boardCategory: string;
  startDateTime: string;
  endDateTime: string;
  createdDateTime: string;
  memberResponse: MemberResponse;
  _links: BoardResponseLink;
}

export interface BoardResponseList {
  _embedded: {
    boardResponseList: Array<BoardResponse>;
  };
  _links: Link;
  page: Page;
}
