import { AuthUtils } from "@/utils/auth";
import { Dictionary } from "vue-router/types/router";
import { PATH } from "@/router/routes_path";
import router from "@/router/index";
import { ROUTES_NAME } from "@/router/routes_name";

class RouterHelper {
  cartId = (): number => parseInt(getParams().cartId, 10);
  orderId = (): number => parseInt(getParams().orderId, 10);
  boardId = (): number => parseInt(getParams().boardId, 10);
  memberId = (): number => parseInt(getParams().memberId, 10);
  productId = (): number => parseInt(getParams().productId, 10);

  async pushWhenException(e: any, path: string) {
    if (!AuthUtils.isUnauthorizedError(e)) {
      if (path === PATH.BACK) {
        router.back();
      } else {
        await router.push(path);
      }
    }
  }

  async pushWhenUnauthorizedError() {
    if (!isLoginPage()) {
      await router.push(PATH.AUTH.LOGIN).catch((e) => {
        if (e && e.name !== "NavigationDuplicated") {
          throw e;
        }
      });
    }
  }

  goToBoardsPage() {
    return router.push(PATH.BOARD.LIST);
  }

  goToMainPage() {
    return router.push(PATH.ROOT);
  }

  goToBackPage() {
    return router.go(-1);
  }

  goToProductPage(productId: number) {
    return router.push({
      name: ROUTES_NAME.PRODUCT.DETAIL,
      params: { productId: productId.toString() },
    });
  }

  goToOrderPage(memberId: number, cartIds: Set<number>) {
    return router.push({
      name: ROUTES_NAME.ORDER.CREATE,
      params: {
        memberId: memberId.toString(),
        cartIds: Array.from(cartIds).toString(),
      },
    });
  }
}

const getParams = (): Dictionary<string> => router.currentRoute.params;
const isLoginPage = () => router.currentRoute.path === PATH.AUTH.LOGIN;

const routerHelper = new RouterHelper();
export default routerHelper;
