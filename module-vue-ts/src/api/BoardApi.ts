import {
  BoardResponseList,
  BoardSearchCondition,
} from "@/interfaces/board/board";
import { PageRequest } from "@/interfaces/common/page";
import { publicAxios } from "@/api/index";
import { AxiosPromise } from "axios";

const boardApi = {
  getBoardResponseList(
    condition?: BoardSearchCondition,
    pageRequest?: PageRequest
  ): AxiosPromise<BoardResponseList> {
    return publicAxios.get("api/boards");
  },
};

export default boardApi;
