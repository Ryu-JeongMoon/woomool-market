import Vue from "vue";
import VueRouter, { RouteConfig } from "vue-router";

Vue.use(VueRouter);

const routes: Array<RouteConfig> = [
  {
    path: "/",
    name: "Home",
  },
  {
    path: "/about",
    name: "About",
    component: () => import("@/views/About.vue"),
  },
  {
    path: "/login",
    name: "LoginPage",
    component: () => import("@/views/member/login/LoginPage.vue"),
  },
  {
    path: "/signup",
    name: "SignupPage",
    component: () => import("@/views/member/signup/SignupPage.vue"),
  },
  {
    path: "/oauth2",
    name: "OAuth2LoginPage",
    component: () => import("@/views/member/login/OAuth2LoginPage.vue"),
  },
  {
    path: "*",
    component: () => import("@/views/error/NotFoundPage.vue"),
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
