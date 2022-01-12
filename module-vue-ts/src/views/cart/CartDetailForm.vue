<template>
  <li>
    <v-flex>
      <v-checkbox
        label="pick"
        @click="submitCallback(cartId)"
        hide-details
        input-value="true"
      />
    </v-flex>
    <div class="cart-id">
      주문상품 :
      <v-btn @click="goToProductPage(productId)">{{ productName }}</v-btn>
    </div>
    <div class="cart-products">
      YAHO: {{ productId }}<br />
      가격: {{ price }}<br />
      주문수량 : {{ quantity }}<br />
      총 가격 : {{ price * quantity }}
    </div>
    <div class="cart-trash">
      <v-btn @click="deleteCart" color="pink" class="mt-2" small>
        <v-icon>delete_outline</v-icon>
      </v-btn>
    </div>
  </li>
</template>

<script lang="ts">
import Vue, { PropType } from "vue";
import { CartQueryResponse } from "@/interfaces/cart";
import cartApi from "@/api/CartApi";

export default Vue.extend({
  data() {
    return {
      cartId: this.cartQueryResponse.id,
      quantity: this.cartQueryResponse.quantity,
      price: this.cartQueryResponse.productQueryResponse.price,
      productId: this.cartQueryResponse.productQueryResponse.id,
      productName: this.cartQueryResponse.productQueryResponse.name,
    };
  },

  props: {
    cartQueryResponse: {
      type: Object as PropType<CartQueryResponse>,
      required: true,
    },
    goToProductPage: {
      type: Function as PropType<(productId: number) => void>,
      required: true,
    },
    submitCallback: {
      type: Function as PropType<(id: number) => void>,
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
