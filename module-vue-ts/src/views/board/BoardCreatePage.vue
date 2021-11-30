<template>
  <div class="form-container">
    <BoardCreateForm :submitClickCallback="submit" @back="moveToMainPage" />
    <ScrollToTop />
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import routerHelper from "@/router/RouterHelper";
import BoardCreateForm from "@/views/board/BoardCreateForm.vue";
import ScrollToTop from "@/components/button/ScrollToTop.vue";
import { BoardRequest } from "@/interfaces/board";
import { BoardActionTypes } from "@/store/type/actionTypes";

export default Vue.extend({
  components: { ScrollToTop, BoardCreateForm },

  data() {
    return {
      board: "게시판",
    };
  },

  methods: {
    moveToMainPage() {
      routerHelper.goToBoardsPage();
    },

    submit(boardRequest: BoardRequest) {
      return this.$store
        .dispatch(BoardActionTypes.REQUEST_BOARD_CREATE, boardRequest)
        .then(() => {
          this.$store.dispatch(BoardActionTypes.REQUEST_BOARD_LIST);
          this.moveToMainPage();
        });
    },
  },
});
</script>

<style scoped lang="scss"></style>
