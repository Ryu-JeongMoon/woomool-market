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
    path: "*",
    component: () => import("@/views/error/NotFoundPage.vue"),
  },
];

export default routes;
