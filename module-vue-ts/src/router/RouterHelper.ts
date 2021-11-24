import { AuthUtils } from "@/utils/auth";
import { Dictionary } from "vue-router/types/router";
import { generateParamPath, PATH } from "@/router/routes_path";
import router from "@/router/index";

class RouterHelper {
  productId = (): number => parseInt(getParams().productId, 10);
  memberId = (): number => parseInt(getParams().memberId, 10);
  boardId = (): number => parseInt(getParams().boardId, 10);
  orderId = (): number => parseInt(getParams().orderId, 10);
  cartId = (): number => parseInt(getParams().cartId, 10);

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
      await router.push(PATH.LOGIN).catch((e) => {
        if (e && e.name !== "NavigationDuplicated") {
          throw e;
        }
      });
    }
  }

  moveToClubMainPage() {
    const boardId = this.boardId();
    if (isNaN(boardId)) {
      throw new Error("Board-Id should be Number");
    }
    return router.push(generateParamPath(PATH.ROOT, [boardId]));
  }
}

const getParams = (): Dictionary<string> => router.currentRoute.params;
const isLoginPage = () => router.currentRoute.path === PATH.LOGIN;

const routerHelper = new RouterHelper();
export default routerHelper;
