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
            :submitCallback="handleCartExclusion"
          />
        </ul>
      </div>
      <div class="cart-order">
        <v-btn @click="goToOrderPage" color="info" class="mt-2" large>
          <v-icon>shop</v-icon> 주문하기
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

export default Vue.extend({
  components: { CartDetailForm, LoadingSpinner },

  data: () => ({
    isLoading: false,
    cartResponsePage: {} as CartResponsePage,
    cartIdsToBeOrdered: {} as Set<number>,
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

      this.cartIdsToBeOrdered = new Set(
        this.cartResponsePage._embedded.cartQueryResponseList.map(
          (cart) => cart.id
        )
      );
    },

    handleCartExclusion(toBeInOrOut: number) {
      if (this.cartIdsToBeOrdered.has(toBeInOrOut)) {
        this.cartIdsToBeOrdered.delete(toBeInOrOut);
      } else {
        this.cartIdsToBeOrdered.add(toBeInOrOut);
      }
    },

    goToMainPage() {
      routerHelper.goToMainPage();
    },

    goToProductPage(productId: number) {
      routerHelper.goToProductPage(productId);
    },

    goToOrderPage() {
      routerHelper.goToOrderPage(this.memberId, this.cartIdsToBeOrdered);
    },
  },
});
</script>

<style scoped lang="scss"></style>
