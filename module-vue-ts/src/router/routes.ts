import { RouteConfig } from "vue-router";
import { PATH } from "@/router/routes_path";

const routes: Array<RouteConfig> = [
  {
    path: PATH.ROOT,
    name: "Home",
  },
  {
    path: PATH.ABOUT,
    name: "About",
    component: () => import("@/views/About.vue"),
  },
  {
    path: PATH.LOGIN,
    name: "LoginPage",
    component: () => import("@/views/member/LoginPage.vue"),
  },
  {
    path: PATH.SIGNUP,
    name: "SignupPage",
    component: () => import("@/views/member/SignupPage.vue"),
  },
  {
    path: PATH.OAUTH2,
    name: "OAuth2LoginPage",
    component: () => import("@/views/member/OAuth2LoginPage.vue"),
  },
  {
    path: PATH.BOARDS,
    name: "BoardListPage",
    component: () => import("@/views/board/BoardListPage.vue"),
  },
  {
    path: "*",
    component: () => import("@/views/error/NotFoundPage.vue"),
  },
];

export default routes;

/*
사용되지 않을 리소스들이 미리 다운로드되지 않게 코드 스플리팅 (dynamic importing) 을 이용해 필요할때 가져오게 함
 */
