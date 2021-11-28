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
    path: PATH.AUTH.LOGIN,
    name: "Login",
    component: () => import("@/views/auth/LoginPage.vue"),
  },
  {
    path: PATH.AUTH.OAUTH2,
    name: "OAuth2Login",
    component: () => import("@/views/member/OAuth2LoginPage.vue"),
  },
  {
    name: "Signup",
    path: PATH.AUTH.SIGNUP,
    component: () => import("@/views/member/SignupPage.vue"),
  },

  {
    name: "Members",
    path: PATH.MEMBER.LIST,
    component: () => import("@/views/member/MemberListPage.vue"),
  },
  {
    name: "DetailMembers",
    path: PATH.MEMBER.DETAIL,
    component: () => import("@/views/member/MemberDetailPage.vue"),
  },
  {
    name: "ModifyMembers",
    path: PATH.MEMBER.MODIFY,
    component: () => import("@/views/member/MemberModifyPage.vue"),
  },

  {
    name: "Boards",
    path: PATH.BOARD.LIST,
    component: () => import("@/views/board/BoardListPage.vue"),
    props: (route) => ({
      condition: route.query.condition,
      pageable: route.query.pageable,
    }),
  },
  {
    name: "CreateBoards",
    path: PATH.BOARD.CREATE,
    component: () => import("@/views/board/BoardCreatePage.vue"),
  },
  {
    name: "DetailBoards",
    path: PATH.BOARD.DETAIL,
    component: () => import("@/views/board/BoardDetailPage.vue"),
    props: (route) => ({
      boardId: Number(route.params.boardId),
    }),
  },
  {
    name: "ModifyBoards",
    path: PATH.BOARD.MODIFY,
    component: () => import("@/views/board/BoardModifyPage.vue"),
  },

  {
    name: "Carts",
    path: PATH.CART.LIST,
    component: () => import("@/views/cart/CartListPage.vue"),
  },
  {
    name: "CreateCarts",
    path: PATH.CART.CREATE,
    component: () => import("@/views/cart/CartCreatePage.vue"),
  },
  {
    name: "DetailCarts",
    path: PATH.CART.DETAIL,
    component: () => import("@/views/cart/CartDetailPage.vue"),
  },
  {
    name: "ModifyCarts",
    path: PATH.CART.MODIFY,
    component: () => import("@/views/cart/CartModifyPage.vue"),
  },

  {
    name: "Products",
    path: PATH.PRODUCT.LIST,
    component: () => import("@/views/product/ProductListPage.vue"),
  },
  {
    name: "CreateProducts",
    path: PATH.PRODUCT.CREATE,
    component: () => import("@/views/product/ProductCreatePage.vue"),
  },
  {
    name: "DetailProducts",
    path: PATH.PRODUCT.DETAIL,
    component: () => import("@/views/product/ProductDetailPage.vue"),
  },
  {
    name: "ModifyProducts",
    path: PATH.PRODUCT.MODIFY,
    component: () => import("@/views/product/ProductModifyPage.vue"),
  },

  {
    name: "Orders",
    path: PATH.ORDER.LIST,
    component: () => import("@/views/order/OrderListPage.vue"),
  },
  {
    name: "CreateOrders",
    path: PATH.ORDER.CREATE,
    component: () => import("@/views/order/OrderCreatePage.vue"),
  },
  {
    name: "DetailOrders",
    path: PATH.ORDER.DETAIL,
    component: () => import("@/views/order/OrderDetailPage.vue"),
  },
  {
    name: "ModifyOrders",
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
