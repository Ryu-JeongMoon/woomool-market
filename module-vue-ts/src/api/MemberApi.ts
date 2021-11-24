import { LoginRequest, MemberRequest } from "@/interfaces/member/member";
import { publicAxios } from "@/api/index";

const memberApi = {
  signup(memberRequest: MemberRequest) {
    return publicAxios.post("/api/members", memberRequest);
  },

  login(loginRequest: LoginRequest) {
    return publicAxios.post("/api/auth/login", loginRequest);
  },
};

export default memberApi;
