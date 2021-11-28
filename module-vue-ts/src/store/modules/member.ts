import { LocalStorageUtils } from "@/utils/localstorage";

export const state = {
  username: LocalStorageUtils.getUsernameFromLocalStorage() || "",
};
export type MemberState = typeof state;

export const getters = {
  isLogin(): boolean {
    return state.username !== "";
  },
};
export type MemberGetters = typeof getters;

export const mutations = {
  setUsername(state: MemberState, username: string): void {
    state.username = username;
  },

  clearUsername(state: MemberState): void {
    state.username = "";
  },
};
export type MemberMutations = typeof mutations;

export const actions = {};
export type MemberActions = typeof actions;
