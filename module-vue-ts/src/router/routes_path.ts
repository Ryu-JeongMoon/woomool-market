import _ from "lodash";

export const PATH = {
  ROOT: "/",
  ABOUT: "/about",
  LOGIN: "/login",
  SIGNUP: "/signup",
  OAUTH2: "/oauth2",

  BOARDS: "/boards",
  BOARD_DETAIL: "/boards/:boardId",

  BACK: "/back",
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
