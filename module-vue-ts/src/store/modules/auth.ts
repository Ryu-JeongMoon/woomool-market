import { AuthUtils } from "@/utils/auth";
import _ from "lodash";

export const state = {
  appToken: AuthUtils.getAppToken() as string,
};
export type AuthState = typeof state;

export const getters = {
  hasToken(state: AuthState): boolean {
    return !_.isEmpty(state.appToken);
  },
};

export type AuthGetters = typeof getters;
