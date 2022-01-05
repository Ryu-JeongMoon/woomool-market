import { CartRequest, CartResponse, CartResponsePage } from "@/interfaces/cart";
import { privateAxios } from "@/api/index";
import ResponseConverter from "@/api/converter/ResponseConverter";
import { Pageable } from "@/interfaces/common/page";

const cartApi = {
  getBy(memberId: number, cartId: number): Promise<CartResponse> {
    return privateAxios
      .get(`/api/carts/${memberId}/${cartId}`)
      .then(ResponseConverter.retrieveData);
  },

  getPageBy(memberId: number, pageable?: Pageable): Promise<CartResponsePage> {
    return privateAxios
      .get(`/api/carts/${memberId}`, { params: { ...pageable } })
      .then(ResponseConverter.retrieveData);
  },

  addBy(memberId: number, cartRequest: CartRequest): Promise<number> {
    return privateAxios
      .post(`/api/carts/${memberId}`, cartRequest)
      .then(ResponseConverter.retrieveStatus);
  },

  remove(memberId: number, cartId: number): Promise<number> {
    return privateAxios
      .delete(`/api/carts/${memberId}/${cartId}`)
      .then(ResponseConverter.retrieveStatus);
  },

  removeAll(memberId: number): Promise<number> {
    return privateAxios
      .delete(`/api/carts/${memberId}`)
      .then(ResponseConverter.retrieveStatus);
  },

  getPageForAdminBy(pageable?: Pageable): Promise<CartResponsePage> {
    return privateAxios
      .get("/api/carts/admin", { params: { ...pageable } })
      .then(ResponseConverter.retrieveData);
  },
};

export default cartApi;
