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
      <v-btn @click="order" color="info" class="mt-2" large>
        <v-icon>paid</v-icon> 결제하기
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
import orderApi from "@/api/OrderApi";
import { MESSAGE } from "@/utils/common/messages";
import { OrderRequest } from "@/interfaces/order";

export default Vue.extend({
  components: { CartDetailForm, LoadingSpinner },

  data: () => ({
    isLoading: false,
    convertedCartIds: {} as Set<number>,
    cartIdsToBePaid: {} as Set<number>,
    cartResponsePage: {} as CartResponsePage,
    cartQueryResponseList: {} as CartQueryResponse[],
  }),

  props: {
    memberId: {
      type: Number,
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
        cartIds: Array.from(this.convertedCartIds),
      };
      this.cartResponsePage = await cartApi
        .getPickedBy(Number(this.memberId), cartIdRequest)
        .finally(() => LoadingHelper.switchLoadingState(this.isLoading));

      this.cartQueryResponseList =
        this.cartResponsePage._embedded.cartQueryResponseList;

      this.cartIdsToBePaid = this.convertedCartIds;
    },

    handleOrderExclusion(toBeInOrOut: number) {
      if (this.cartIdsToBePaid.has(toBeInOrOut)) {
        this.cartIdsToBePaid.delete(toBeInOrOut);
      } else {
        this.cartIdsToBePaid.add(toBeInOrOut);
      }
    },

    goToProductPage(productId: number) {
      routerHelper.goToProductPage(productId);
    },

    async order() {
      if (this.cartIdsToBePaid.size === 0) {
        return alert(MESSAGE.INVALID_REQUEST);
      }

      const orderRequest: OrderRequest = {
        memberId: this.memberId,
        cartIds: Array.from(this.cartIdsToBePaid),
      };

      await orderApi.order(orderRequest).then((status) => {
        if (status === 201) {
          alert(MESSAGE.ORDER.COMPLETED);
          // TODO, 주문 완료 화면으로 이동
        } else {
          alert(MESSAGE.INVALID_REQUEST);
        }
      });
    },
  },
});
</script>

<style scoped lang="scss"></style>
