import _ from "lodash";

export const PATH = {
  ROOT: "/",
  ABOUT: "/about",

  AUTH: {
    LOGIN: "/login",
    OAUTH2: "/oauth2",
    SIGNUP: "/signup",
  },

  MEMBER: {
    LIST: "/members",
    DETAIL: "/members/:memberId",
    MODIFY: "/members/:memberId/modify",
  },

  BOARD: {
    LIST: "/boards",
    CREATE: "/boards/write",
    DETAIL: "/boards/:boardId",
    MODIFY: "/boards/:boardId/modify",
  },

  PRODUCT: {
    LIST: "/products",
    CREATE: "/products/create",
    DETAIL: "/products/:productId",
    MODIFY: "/products/:productId/modify",
  },

  CART: {
    ADMIN: "/carts/admin",
    CREATE: "/carts/create",
    DETAIL: "/carts/:memberId",
  },

  ORDER: {
    LIST: "/orders",
    CREATE: "/orders/create",
    DETAIL: "/orders/:orderId",
    MODIFY: "/orders/:orderId/modify",
  },

  BACK: "/back",
};

export const getChildRoutePath = (path: string): string => {
  const split = path.split("/");
  return split[split.length - 1];
};

export const generateParamPath = (path: string, params: number[]): string => {
  if (_.isEmpty(path) || _.isEmpty(params)) {
    throw Error(
      `path or params must not be empty. path: ${path}, params: ${params}`
    );
  }
  let paramIndex = 0;
  return path
    .split("/")
    .map((token: string) => {
      if (token.startsWith(":")) {
        // eslint-disable-next-line no-plusplus
        return params[paramIndex++].toString();
      }
      return token;
    })
    .join("/");
};
