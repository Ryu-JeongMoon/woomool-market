import { RouteConfig } from "vue-router";
import { PATH } from "@/router/routes_path";
import { ROUTES_NAME } from "@/router/routes_name";

const routes: Array<RouteConfig> = [
  {
    name: ROUTES_NAME.ROOT,
    path: PATH.ROOT,
  },
  {
    name: ROUTES_NAME.ABOUT,
    path: PATH.ABOUT,
    component: () => import("@/views/About.vue"),
  },
  {
    name: ROUTES_NAME.AUTH.LOGIN,
    path: PATH.AUTH.LOGIN,
    component: () => import("@/views/auth/LoginPage.vue"),
  },
  {
    name: ROUTES_NAME.AUTH.OAUTH2,
    path: PATH.AUTH.OAUTH2,
    component: () => import("@/views/member/OAuth2LoginPage.vue"),
  },
  {
    name: ROUTES_NAME.AUTH.SIGNUP,
    path: PATH.AUTH.SIGNUP,
    component: () => import("@/views/member/SignupPage.vue"),
  },

  {
    name: ROUTES_NAME.MEMBER.LIST,
    path: PATH.MEMBER.LIST,
    component: () => import("@/views/member/MemberListPage.vue"),
  },
  {
    name: ROUTES_NAME.MEMBER.DETAIL,
    path: PATH.MEMBER.DETAIL,
    props: true,
    component: () => import("@/views/member/MemberDetailPage.vue"),
  },
  {
    name: ROUTES_NAME.MEMBER.MODIFY,
    path: PATH.MEMBER.MODIFY,
    component: () => import("@/views/member/MemberModifyPage.vue"),
    props: (route) => ({
      memberId: route.params.memberId,
    }),
  },

  {
    name: ROUTES_NAME.BOARD.LIST,
    path: PATH.BOARD.LIST,
    component: () => import("@/views/board/BoardListPage.vue"),
    props: (route) => ({
      condition: route.query.condition,
      pageable: route.query.pageable,
    }),
  },
  {
    name: ROUTES_NAME.BOARD.CREATE,
    path: PATH.BOARD.CREATE,
    component: () => import("@/views/board/BoardCreatePage.vue"),
  },
  {
    name: ROUTES_NAME.BOARD.DETAIL,
    path: PATH.BOARD.DETAIL,
    component: () => import("@/views/board/BoardDetailPage.vue"),
    props: (route) => ({
      boardId: Number(route.params.boardId),
    }),
  },
  {
    name: ROUTES_NAME.BOARD.MODIFY,
    path: PATH.BOARD.MODIFY,
    component: () => import("@/views/board/BoardModifyPage.vue"),
  },

  {
    name: ROUTES_NAME.CART.DETAIL,
    path: PATH.CART.DETAIL,
    component: () => import("@/views/cart/CartDetailPage.vue"),
    props: (route) => ({
      memberId: Number(route.params.memberId),
      pageable: route.params.pageable,
    }),
  },
  {
    name: ROUTES_NAME.CART.ADMIN,
    path: PATH.CART.ADMIN,
    props: true,
    component: () => import("@/views/cart/CartListPage.vue"),
  },

  {
    name: ROUTES_NAME.PRODUCT.LIST,
    path: PATH.PRODUCT.LIST,
    component: () => import("@/views/product/ProductListPage.vue"),
  },
  {
    name: ROUTES_NAME.PRODUCT.CREATE,
    path: PATH.PRODUCT.CREATE,
    component: () => import("@/views/product/ProductCreatePage.vue"),
  },
  {
    name: ROUTES_NAME.PRODUCT.DETAIL,
    path: PATH.PRODUCT.DETAIL,
    props: true,
    component: () => import("@/views/product/ProductDetailPage.vue"),
  },
  {
    name: ROUTES_NAME.PRODUCT.MODIFY,
    path: PATH.PRODUCT.MODIFY,
    component: () => import("@/views/product/ProductModifyPage.vue"),
  },

  {
    name: ROUTES_NAME.ORDER.LIST,
    path: PATH.ORDER.LIST,
    component: () => import("@/views/order/OrderListPage.vue"),
  },
  {
    name: ROUTES_NAME.ORDER.CREATE,
    path: PATH.ORDER.CREATE,
    component: () => import("@/views/order/OrderCreatePage.vue"),
    props: true,
  },
  {
    name: ROUTES_NAME.ORDER.DETAIL,
    path: PATH.ORDER.DETAIL,
    component: () => import("@/views/order/OrderDetailPage.vue"),
  },
  {
    name: ROUTES_NAME.ORDER.MODIFY,
    path: PATH.ORDER.MODIFY,
    component: () => import("@/views/order/OrderModifyPage.vue"),
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
