import _ from "lodash";
import authApi from "@/api/AuthApi";
import { AuthUtils } from "@/utils/auth";
import { CookieUtils } from "@/utils/cookies";
import { LoginRequest } from "@/interfaces/member";
import { AuthActionContext } from "@/store/type/actionContextTypes";

export const state = {
  appToken: AuthUtils.getAppToken() as string,
  accessToken: CookieUtils.getAccessTokenFromCookie() || "",
  refreshToken: CookieUtils.getRefreshTokenFromCookie() || "",
};
export type AuthState = typeof state;

export const getters = {
  hasToken(state: AuthState): boolean {
    return !_.isEmpty(state.accessToken);
  },
};
export type AuthGetters = typeof getters;

export const mutations = {
  setAccessToken(state: AuthState, accessToken: string) {
    state.accessToken = accessToken;
  },

  setRefreshToken(state: AuthState, refreshToken: string) {
    state.refreshToken = refreshToken;
  },
};
export type AuthMutations = typeof mutations;

export const actions = {
  async LOGIN(context: AuthActionContext, loginRequest: LoginRequest) {
    const response = await authApi.login(loginRequest);
    console.log(response.accessToken);
    CookieUtils.setAccessTokenToCookie(response.accessToken);
    CookieUtils.setRefreshTokenToCookie(response.refreshToken);
  },
};
export type AuthActions = typeof actions;
