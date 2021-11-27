<template>
  <div>
    <header>
      <h1 class="title">List</h1>
    </header>
    <main>
      <ul>
        <LoadingSpinner v-if="isLoading" />
        <BoardListForm
          v-else
          :boardResponseList="boardResponseList"
          :page="page"
          :links="links"
        />
      </ul>
    </main>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import BoardListForm from "@/views/board/BoardListForm.vue";
import { BoardResponse, BoardSearchCondition } from "@/interfaces/board";
import { Page, Pageable } from "@/interfaces/common/page";
import { Links } from "@/interfaces/common/links";
import boardApi from "@/api/BoardApi";
import LoadingSpinner from "@/components/common/LoadingSpinner.vue";

export default Vue.extend({
  components: { LoadingSpinner, BoardListForm },

  data() {
    return {
      boardResponseList: [] as BoardResponse[],
      page: {} as Page,
      links: {} as Links,
      isLoading: false,
    };
  },

  created() {
    this.fetchBoardResponseList();
  },

  methods: {
    async fetchBoardResponseList(
      condition?: BoardSearchCondition,
      pageable?: Pageable
    ) {
      this.startLoading();

      const boardResponses = await boardApi
        .getBoardList(condition, pageable)
        .finally(() => this.endLoading());

      console.log(boardResponses);

      this.boardResponseList = boardResponses._embedded.boardResponseList;
      this.page = boardResponses.page;
      this.links = boardResponses._links;
    },

    startLoading() {
      this.isLoading = true;
    },

    endLoading() {
      this.isLoading = false;
    },
  },
});
</script>

<style scoped></style>
