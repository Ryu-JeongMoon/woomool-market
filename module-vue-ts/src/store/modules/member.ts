export const state = {
  username: "",
};
export type MemberState = typeof state;

export const getters = {
  isLogin() {
    return state.username !== "";
  },
};
export type MemberGetters = typeof getters;

export const mutations = {
  setUsername(state: MemberState, username: string) {
    state.username = username;
  },

  clearUsername(state: MemberState) {
    state.username = "";
  },
};
export type MemberMutations = typeof mutations;

export const actions = {};
export type MemberActions = typeof actions;
