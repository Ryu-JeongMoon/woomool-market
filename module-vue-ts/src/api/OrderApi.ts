import { Pageable } from "@/interfaces/common/page";
import { privateAxios } from "@/api/index";
import ResponseConverter from "@/api/converter/ResponseConverter";
import {
  OrderDeleteRequest,
  OrderQueryResponse,
  OrderResponsePage,
  OrderRequest,
  OrderSearchCondition,
} from "@/interfaces/order";

const orderApi = {
  getPageBy(
    memberId: number,
    pageable?: Pageable
  ): Promise<OrderQueryResponse> {
    return privateAxios
      .get(`/api/orders/${memberId}`, { params: { ...pageable } })
      .then(ResponseConverter.retrieveData);
  },

  order(orderRequest: OrderRequest): Promise<number> {
    return privateAxios
      .post("/api/orders", orderRequest)
      .then(ResponseConverter.retrieveStatus);
  },

  cancel(
    memberId: number,
    orderDeleteRequest: OrderDeleteRequest
  ): Promise<number> {
    return privateAxios
      .delete(`/api/orders/${memberId}`, { data: { orderDeleteRequest } })
      .then(ResponseConverter.retrieveStatus);
  },

  getPageForAdminBy(
    condition?: OrderSearchCondition,
    pageable?: Pageable
  ): Promise<OrderResponsePage> {
    return privateAxios
      .get("/api/orders/admin", { params: { ...condition, ...pageable } })
      .then(ResponseConverter.retrieveData);
  },
};

export default orderApi;
