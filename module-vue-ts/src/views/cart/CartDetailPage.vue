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
            :submitCallBack="goToProductPage"
          />
        </ul>
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
import CommonHeader from "@/components/header/CommonHeader.vue";
import MenuHeader from "@/components/header/MenuHeader.vue";

export default Vue.extend({
  components: { CartDetailForm, LoadingSpinner },

  data: () => ({
    isLoading: false,
    cartResponsePage: {} as CartResponsePage,
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

    goToProductPage() {
      //TODO
    },
  },
});
</script>

<style scoped lang="scss"></style>
