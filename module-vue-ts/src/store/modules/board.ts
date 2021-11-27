import {
  BoardRequest,
  BoardResponse,
  BoardResponseList,
} from "@/interfaces/board";
import StateInitializer from "@/store/utils/Initializer";
import { Page } from "@/interfaces/common/page";
import { BoardActionContext } from "@/store/type/actionContextTypes";
import boardApi from "@/api/BoardApi";

export const state = {
  boardPage: StateInitializer.page() as Page,
  boardResponse: {} as BoardResponse,
  boardResponseList: {} as BoardResponseList,
};
export type BoardState = typeof state;

export const mutations = {};
export type BoardMutations = typeof mutations;

export const actions = {
  async CREATE_BOARD(context: BoardActionContext, boardRequest: BoardRequest) {
    return await boardApi.createBoard(boardRequest);
  },
};
export type BoardActions = typeof actions;
