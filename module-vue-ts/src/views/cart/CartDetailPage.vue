<template>
  <div>
    <h1>장바구니 상세 페이지</h1>
    <v-container>
      <div class="main list-container contents">
        <LoadingSpinner v-if="isLoading" />
        <ul v-else>
          <CartDetailForm
            v-for="cartQueryResponse in cartResponsePage._embedded
              .cartQueryResponseList"
            :key="cartQueryResponse.id"
            :cartQueryResponse="cartQueryResponse"
            :goToProductPage="goToProductPage"
          />
        </ul>
      </div>
      <div class="cart-order">
        <v-btn @click="goToOrderPage" color="info" class="mt-2" large>
          <v-icon>shop</v-icon>
        </v-btn>
      </div>
    </v-container>
  </div>
</template>

<script lang="ts">
import Vue, { PropType } from "vue";
import LoadingSpinner from "@/components/common/LoadingSpinner.vue";
import routerHelper from "@/router/RouterHelper";
import { CartResponsePage } from "@/interfaces/cart";
import { LoadingHelper } from "@/utils/loading";
import cartApi from "@/api/CartApi";
import CartDetailForm from "@/views/cart/CartDetailForm.vue";
import { Pageable } from "@/interfaces/common/page";
import { PATH } from "@/router/routes_path";
import { ROUTES_NAME } from "@/router/routes_name";

export default Vue.extend({
  components: { CartDetailForm, LoadingSpinner },

  data: () => ({
    isLoading: false,
    cartResponsePage: {} as CartResponsePage,
    cartIds: {} as number[],
  }),

  props: {
    memberId: {
      type: Number,
      required: true,
    },
    pageable: {
      type: {} as PropType<Pageable>,
      required: false,
    },
  },

  created() {
    this.fetchCartResponse();
  },

  methods: {
    async fetchCartResponse() {
      LoadingHelper.switchLoadingState(this.isLoading);

      this.cartResponsePage = await cartApi
        .getPageBy(this.memberId, this.pageable)
        .finally(() => LoadingHelper.switchLoadingState(this.isLoading));
    },

    goToMainPage() {
      routerHelper.goToMainPage();
    },

    goToProductPage(productId: number) {
      this.$router.push({
        name: ROUTES_NAME.PRODUCT.DETAIL,
        params: { productId: productId.toString() },
      });
    },

    goToOrderPage() {
      this.$router.push({ path: PATH.ORDER.CREATE, params: {} });
    },
  },
});
</script>

<style scoped lang="scss"></style>
