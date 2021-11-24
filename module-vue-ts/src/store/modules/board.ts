import { BoardResponse, BoardResponseList } from "@/interfaces/board/board";
import StateInitializer from "@/store/utils/Initializer";
import { Page } from "@/interfaces/common/page";

export const state = {
  boardPage: StateInitializer.page() as Page,
  boardResponse: {} as BoardResponse,
  boardResponseList: {} as BoardResponseList,
};
export type BoardState = typeof state;

export const mutations = {};
export type BoardMutations = typeof mutations;

export const actions = {};
export type BoardActions = typeof actions;
