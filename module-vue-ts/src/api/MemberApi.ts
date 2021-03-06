import {
  MemberModelResponse,
  MemberRequest,
  PagedMemberResponse,
} from "@/interfaces/member";
import { privateAxios, publicAxios } from "@/api/index";
import ResponseConverter from "@/api/converter/ResponseConverter";
import { BoardSearchCondition } from "@/interfaces/board";
import { Pageable } from "@/interfaces/common/page";
import { PATH } from "@/router/routes_path";

const memberApi = {
  getIdBy(username: string): Promise<number> {
    return privateAxios
      .get("/api/members", { params: { email: username } })
      .then(ResponseConverter.retrieveData);
  },

  signup(memberRequest: MemberRequest): Promise<MemberModelResponse> {
    return publicAxios
      .post("/api/members", memberRequest)
      .then(ResponseConverter.retrieveData);
  },

  getMemberModelResponse(memberId: number): Promise<MemberModelResponse> {
    return privateAxios
      .get(`/api/members/${memberId}`)
      .then(ResponseConverter.retrieveData);
  },

  getMemberList(
    condition?: BoardSearchCondition,
    pageRequest?: Pageable
  ): Promise<PagedMemberResponse> {
    return privateAxios
      .get("/api/members/admin", { params: { condition, pageRequest } })
      .then(ResponseConverter.retrieveData);
  },
};

export default memberApi;
