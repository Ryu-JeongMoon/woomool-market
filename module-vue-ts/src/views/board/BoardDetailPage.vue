<template>
  <div>
    <header>상세 페이지</header>
    <v-main>
      <LoadingSpinner v-if="isLoading" />
      <BoardDetailForm
        :submit-callback="goToBoardsPage"
        :boardResponse="boardResponse"
      />
    </v-main>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import BoardDetailForm from "@/views/board/BoardDetailForm.vue";
import boardApi from "@/api/BoardApi";
import { BoardResponse } from "@/interfaces/board";
import LoadingSpinner from "@/components/common/LoadingSpinner.vue";
import { LoadingHelper } from "@/utils/loading";
import CommonHeader from "@/components/header/CommonHeader.vue";
import routerHelper from "@/router/RouterHelper";

export default Vue.extend({
  components: { BoardDetailForm, LoadingSpinner },

  data: () => {
    return {
      isLoading: false,
      boardResponse: {} as BoardResponse,
    };
  },

  props: {
    boardId: {
      type: Number,
      required: true,
    },
  },

  created() {
    this.fetchBoardResponse();
  },

  methods: {
    async fetchBoardResponse() {
      LoadingHelper.switchLoadingState(this.isLoading);
      this.boardResponse = await boardApi
        .getBoard(this.boardId)
        .finally(() => LoadingHelper.switchLoadingState(this.isLoading));
    },

    goToBoardsPage() {
      routerHelper.goToBoardsPage();
    },
  },
});
</script>

<style scoped lang="scss"></style>
