<template>
  <li>
    <div class="cart-id">
      주문상품 :
      <a @onclick="submitCallBack">{{ productName }}</a>
    </div>
    <div class="cart-products">
      가격: {{ price }}<br />
      주문수량 : {{ cartQueryResponse.quantity }}<br />
      총 가격 : {{ price * quantity }}
    </div>
    <div class="cart-trash">
      <v-btn @click="deleteCart" color="info" class="mt-2" small>
        <v-icon>delete_outline</v-icon>
      </v-btn>
    </div>
  </li>
</template>

<script lang="ts">
import Vue, { PropType } from "vue";
import {
  CartQueryResponse,
  CartResponse,
  CartResponsePage,
} from "@/interfaces/cart";
import { Page } from "@/interfaces/common/page";
import { Links } from "@/interfaces/common/links";
import cartApi from "@/api/CartApi";

export default Vue.extend({
  data() {
    return {
      productName: this.cartQueryResponse.productQueryResponse.name,
      price: this.cartQueryResponse.productQueryResponse.price,
      quantity: this.cartQueryResponse.quantity,
    };
  },

  props: {
    cartQueryResponse: {
      type: {} as PropType<CartQueryResponse>,
      required: true,
    },
    submitCallBack: {
      type: Function as PropType<() => void>,
      required: true,
    },
  },

  methods: {
    async deleteCart() {
      await cartApi
        .remove(
          this.cartQueryResponse.memberQueryResponse.id,
          this.cartQueryResponse.id
        )
        .then((status) => {
          if (status === 204) {
            alert("삭제되었습니다");
          } else {
            alert("잘못된 요청입니다");
          }
        });
    },
  },
});
</script>

<style scoped lang="scss"></style>
