import {
  MemberModelResponse,
  MemberRequest,
  PagedMemberResponse,
} from "@/interfaces/member";
import { privateAxios, publicAxios } from "@/api/index";
import ResponseConverter from "@/api/converter/ResponseConverter";
import { BoardSearchCondition } from "@/interfaces/board";
import { Pageable } from "@/interfaces/common/page";

const memberApi = {
  signup(memberRequest: MemberRequest): Promise<MemberModelResponse> {
    return publicAxios
      .post("/api/members", memberRequest)
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
