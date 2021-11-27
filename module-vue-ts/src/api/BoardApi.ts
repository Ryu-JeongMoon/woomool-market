import {
  BoardModifyRequest,
  BoardRequest,
  BoardResponse,
  BoardResponseList,
  BoardSearchCondition,
} from "@/interfaces/board";
import { Pageable } from "@/interfaces/common/page";
import { privateAxios, publicAxios } from "@/api/index";
import ResponseConverter from "@/api/converter/ResponseConverter";

const boardApi = {
  getBoard(boardId: number): Promise<BoardResponse> {
    return publicAxios
      .get(`/api/boards/${boardId}`)
      .then(ResponseConverter.retrieveData);
  },

  getBoardList(
    condition?: BoardSearchCondition,
    pageable?: Pageable
  ): Promise<BoardResponseList> {
    return publicAxios
      .get(`/api/boards`, {
        // params: { page: pageable?.page, size: pageable?.size },
        params: { ...condition, ...pageable },
      })
      .then(ResponseConverter.retrieveData);
  },

  createBoard(boardRequest: BoardRequest): Promise<number> {
    return privateAxios
      .post("/api/boards", boardRequest)
      .then(ResponseConverter.retrieveStatus);
  },

  modifyBoard(
    boardId: number,
    boardModifyRequest: BoardModifyRequest
  ): Promise<BoardResponse> {
    return privateAxios
      .patch(`/api/boards/${boardId}`, boardModifyRequest)
      .then(ResponseConverter.retrieveData);
  },

  deleteBoard(boardId: number): Promise<number> {
    return privateAxios
      .delete(`/api/boards/${boardId}`)
      .then(ResponseConverter.retrieveStatus);
  },

  restoreBoard(boardId: number): Promise<number> {
    return privateAxios
      .get(`/api/boards/deleted/${boardId}`)
      .then(ResponseConverter.retrieveStatus);
  },

  getBoardListForAdmin(
    condition?: BoardSearchCondition,
    pageable?: Pageable
  ): Promise<BoardResponseList> {
    return privateAxios
      .get(`/api/boards/admin`, { params: { condition, pageable } })
      .then(ResponseConverter.retrieveData);
  },
};

export default boardApi;
