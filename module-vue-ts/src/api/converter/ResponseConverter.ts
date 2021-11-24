import { AxiosResponse } from "axios";
import { BoardResponseList } from "@/interfaces/board/board";

export default class ResponseConverter {
  static extractData(response: AxiosResponse<any>) {
    return response && response.data;
  }

  static convertBoardResponseList(data: BoardResponseList): BoardResponseList {
    return {
      _embedded: data._embedded,
      page: data.page,
      _links: data._links,
    };
  }
}
