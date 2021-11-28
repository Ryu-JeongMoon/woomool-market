import { LoginRequest } from "@/interfaces/member";
import { privateAxios, publicAxios } from "@/api/index";
import { TokenResponse } from "@/interfaces/common/auth";
import ResponseConverter from "@/api/converter/ResponseConverter";

const authApi = {
  login(loginRequest: LoginRequest): Promise<TokenResponse> {
    return publicAxios
      .post("/api/auth/login", loginRequest)
      .then(ResponseConverter.retrieveData);
  },

  logout(): Promise<number> {
    return privateAxios
      .post("/api/auth/logout")
      .then(ResponseConverter.retrieveStatus);
  },

  sendEmailVerification(email: string): Promise<number> {
    return publicAxios
      .post("/api/auth/email-verification", { email })
      .then(ResponseConverter.retrieveStatus);
  },

  verifyEmail(authString: string): Promise<number> {
    return publicAxios
      .post("/api/auth/auth-string-verification", {
        authString,
      })
      .then(ResponseConverter.retrieveStatus);
  },
};

export default authApi;
