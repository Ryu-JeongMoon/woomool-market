import Vue from "vue";
import VueRouter from "vue-router";
import routes from "@/router/routes";

Vue.use(VueRouter);

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;

/*
mode: "history" 는 url 에 붙는 #을 제거하려는 목적으로 붙여준다
vue 개발 단계에서는 실제 서버가 아닌 prototype 서버를 이용하고 있기 때문에 #이 붙어있어도 잘 돌아가는데
프로덕션 레벨에서는 apache, nginx 에 server configuration 을 작성해주어야 제대로 돌아간다

그렇다면 #은 왜 붙는걸까?
서버측에서 볼때 localhost:8080/#/blah-blah 로 보여지니까 페이지 이동이 없다고 간주된다
SPA 로써 돌아가기 위해 서버가 인식할 수 없도록 만드는 것
 */
