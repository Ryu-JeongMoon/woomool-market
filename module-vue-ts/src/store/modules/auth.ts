import _ from "lodash";
import authApi from "@/api/AuthApi";
import { AuthUtils } from "@/utils/auth";
import {
  ACCESS_TOKEN_KEY,
  CookieUtils,
  REFRESH_TOKEN_KEY,
} from "@/utils/cookies";
import { LoginRequest } from "@/interfaces/member";
import { AuthActionContext } from "@/store/type/actionContextTypes";
import { LocalStorageUtils } from "@/utils/localstorage";

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
  setAccessToken(state: AuthState, accessToken: string): void {
    state.accessToken = accessToken;
  },

  setRefreshToken(state: AuthState, refreshToken: string): void {
    state.refreshToken = refreshToken;
  },
};
export type AuthMutations = typeof mutations;

export const actions = {
  async LOGIN(
    context: AuthActionContext,
    loginRequest: LoginRequest
  ): Promise<void> {
    await authApi.login(loginRequest).then((response) => {
      LocalStorageUtils.setUsernameToLocalStorage(loginRequest.email);
      CookieUtils.setAccessTokenToCookie(response.accessToken);
      CookieUtils.setRefreshTokenToCookie(response.refreshToken);
    });
  },

  async LOGOUT(): Promise<void> {
    await authApi.logout().finally(() => {
      LocalStorageUtils.removeUsernameFromLocalStorage();
      CookieUtils.deleteCookie(ACCESS_TOKEN_KEY);
      CookieUtils.deleteCookie(REFRESH_TOKEN_KEY);
    });
  },
};
export type AuthActions = typeof actions;
