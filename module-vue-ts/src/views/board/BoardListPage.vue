<template>
  <div>
    <v-app-bar-title class="title">List</v-app-bar-title>
    <v-main>
      <LoadingSpinner v-if="isLoading" />
      <BoardListForm
        v-else
        :boardQueryResponseList="boardQueryResponseList"
        :page="page"
        :links="links"
        @movePage="movePage"
      />
    </v-main>
  </div>
</template>

<script lang="ts">
import Vue, { PropType } from "vue";
import BoardListForm from "@/views/board/BoardListForm.vue";
import { BoardQueryResponse, BoardSearchCondition } from "@/interfaces/board";
import { Page, Pageable } from "@/interfaces/common/page";
import { Links } from "@/interfaces/common/links";
import boardApi from "@/api/BoardApi";
import LoadingSpinner from "@/components/common/LoadingSpinner.vue";
import { LoadingHelper } from "@/utils/loading";

export default Vue.extend({
  components: { LoadingSpinner, BoardListForm },

  data() {
    return {
      boardQueryResponseList: [] as BoardQueryResponse[],
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

      const response = await boardApi
        .getBoardList(condition, pageable)
        .finally(() => LoadingHelper.switchLoadingState(this.isLoading));

      this.boardQueryResponseList = response._embedded.boardQueryResponseList;
      this.page = response.page;
      this.links = response._links;
    },

    movePage(pageable: Pageable) {
      this.fetchBoardResponseList(this.condition, pageable);
    },
  },
});
</script>

<style scoped lang="scss"></style>
