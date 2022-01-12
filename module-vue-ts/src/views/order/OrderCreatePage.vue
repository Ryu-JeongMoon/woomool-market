<template>
  <div>
    <div class="main list-container contents">
      <h1 class="page-header">주문 상세 페이지</h1>
      {{ cartIds }}
      <LoadingSpinner v-if="isLoading" />
      <ul v-else>
        <CartDetailForm
          v-for="cartQueryResponse in cartQueryResponseList"
          :key="cartQueryResponse.id"
          :cartQueryResponse="cartQueryResponse"
          :goToProductPage="goToProductPage"
          :submitCallback="handleOrderExclusion"
        />
      </ul>
    </div>
    <div class="cart-order">
      <v-btn @click="pay(cartIdsToBeOrdered)" color="info" class="mt-2" large>
        <v-icon>paid</v-icon>
      </v-btn>
    </div>
  </div>
</template>

<script lang="ts">
import Vue, { PropType } from "vue";
import LoadingSpinner from "@/components/common/LoadingSpinner.vue";
import { LoadingHelper } from "@/utils/loading";
import cartApi from "@/api/CartApi";
import {
  CartIdRequest,
  CartQueryResponse,
  CartResponsePage,
} from "@/interfaces/cart";
import CartDetailForm from "@/views/cart/CartDetailForm.vue";
import routerHelper from "@/router/RouterHelper";

export default Vue.extend({
  components: { CartDetailForm, LoadingSpinner },

  data: () => ({
    isLoading: false,
    convertedCartIds: {} as Set<number>,
    cartIdsToBeOrdered: {} as Set<number>,
    cartResponsePage: {} as CartResponsePage,
    cartQueryResponseList: {} as CartQueryResponse[],
  }),

  props: {
    memberId: {
      type: String,
      required: true,
    },
    cartIds: {
      type: String as PropType<string>,
      required: true,
    },
  },

  created() {
    this.convertStringToId();
    this.fetchCartResponse();
  },

  methods: {
    convertStringToId() {
      this.convertedCartIds = new Set(
        this.cartIds.split(",").map((s) => Number(s))
      );
    },

    async fetchCartResponse() {
      LoadingHelper.switchLoadingState(this.isLoading);

      const cartIdRequest: CartIdRequest = {
        cartIds: [...this.convertedCartIds],
      };
      this.cartResponsePage = await cartApi
        .getPickedBy(Number(this.memberId), cartIdRequest)
        .finally(() => LoadingHelper.switchLoadingState(this.isLoading));

      this.cartQueryResponseList =
        this.cartResponsePage._embedded.cartQueryResponseList;

      this.cartIdsToBeOrdered = this.convertedCartIds;
    },

    handleOrderExclusion(toBeExcluded: number) {
      if (this.cartIdsToBeOrdered.has(toBeExcluded)) {
        this.cartIdsToBeOrdered.delete(toBeExcluded);
      } else {
        this.cartIdsToBeOrdered.add(toBeExcluded);
      }
    },

    goToProductPage(productId: number) {
      routerHelper.goToProductPage(productId);
    },

    async pay(cartIds: Set<number>) {
      // TODO();
    },
  },
});
</script>

<style scoped lang="scss"></style>
