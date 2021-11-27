import _ from "lodash";

export const PATH = {
  ROOT: "/",
  ABOUT: "/about",

  AUTH: {
    LOGIN: "/login",
    OAUTH2: "/oauth2",
  },

  MEMBER: {
    LIST: "/members",
    SIGNUP: "/members/signup",
    DETAIL: "/members/:memberId",
    MODIFY: "/members/:memberId/edit",
  },

  BOARD: {
    LIST: "/boards",
    CREATE: "/boards/write",
    DETAIL: "/boards/:boardId",
    MODIFY: "/boards/:boardId/edit",
  },

  PRODUCT: {
    LIST: "/products",
    CREATE: "/products/create",
    DETAIL: "/products/:productId",
    MODIFY: "/products/:productId/edit",
  },

  CART: {
    LIST: "/carts",
    CREATE: "/carts/create",
    DETAIL: "/carts/:cartId",
    MODIFY: "/carts/:cartId/edit",
  },

  ORDER: {
    LIST: "/orders",
    CREATE: "/orders/create",
    DETAIL: "/orders/:orderId",
    MODIFY: "/orders/:orderId/edit",
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
