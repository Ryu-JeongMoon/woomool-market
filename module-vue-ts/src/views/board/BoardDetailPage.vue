<template>
  <div>
    <header>상세 페이지</header>
    <v-main>
      <LoadingSpinner v-if="isLoading" />
      <BoardDetailForm :boardResponse="boardResponse" />
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
  },
});
</script>

<style scoped lang="scss"></style>
