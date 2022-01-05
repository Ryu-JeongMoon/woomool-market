import { AxiosResponse } from "axios";
import { BoardQueryResponseList } from "@/interfaces/board";

export default class ResponseConverter {
  static retrieveData(response: AxiosResponse): any {
    return response && response.data;
  }

  static retrieveStatus(response: AxiosResponse): any {
    return response && response.status;
  }

  static convertBoardResponseList(
    data: BoardQueryResponseList
  ): BoardQueryResponseList {
    return {
      _embedded: data._embedded,
      page: data.page,
      _links: data._links,
    };
  }
}
