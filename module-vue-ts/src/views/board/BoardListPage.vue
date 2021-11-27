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
          @movePage="movePage"
        />
      </ul>
    </main>
  </div>
</template>

<script lang="ts">
import Vue, { PropType } from "vue";
import BoardListForm from "@/views/board/BoardListForm.vue";
import { BoardResponse, BoardSearchCondition } from "@/interfaces/board";
import { Page, Pageable } from "@/interfaces/common/page";
import { Links } from "@/interfaces/common/links";
import boardApi from "@/api/BoardApi";
import LoadingSpinner from "@/components/common/LoadingSpinner.vue";
import { LoadingHelper } from "@/utils/loading";

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

  props: {
    condition: {
      type: {} as PropType<BoardSearchCondition>,
      required: false,
    },
    pageable: {
      type: {} as PropType<Pageable>,
      required: false,
    },
  },

  created() {
    this.fetchBoardResponseList(this.condition, this.pageable);
  },

  methods: {
    async fetchBoardResponseList(
      condition?: BoardSearchCondition,
      pageable?: Pageable
    ) {
      LoadingHelper.switchLoadingState(this.isLoading);

      const boardResponses = await boardApi
        .getBoardList(condition, pageable)
        .finally(() => LoadingHelper.switchLoadingState(this.isLoading));

      this.boardResponseList = boardResponses._embedded.boardResponseList;
      this.page = boardResponses.page;
      this.links = boardResponses._links;
    },

    movePage(pageable: Pageable) {
      console.log("yaho");
      this.fetchBoardResponseList(this.condition, pageable);
    },
  },
});
</script>

<style scoped lang="scss"></style>
