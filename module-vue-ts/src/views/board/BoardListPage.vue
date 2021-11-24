<template>
  <div>
    <header>
      <h1 class="title">List</h1>
    </header>
    <main>
      <ul>
        <ListForm
          :boardResponseList="boardResponseList"
          :page="page"
          :links="links"
        ></ListForm>
      </ul>
    </main>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import ListForm from "@/views/board/BoardListForm.vue";
import { BoardResponse, BoardSearchCondition } from "@/interfaces/board/board";
import { Page, PageRequest } from "@/interfaces/common/page";
import { Link } from "@/interfaces/common/link";
import boardApi from "@/api/BoardApi";

export default Vue.extend({
  components: { ListForm },

  data() {
    return {
      boardResponseList: [] as BoardResponse[],
      page: {} as Page,
      links: {} as Link,
    };
  },

  created() {
    this.fetchBoardResponseList();
  },

  methods: {
    async fetchBoardResponseList(
      condition?: BoardSearchCondition,
      pageable?: PageRequest
    ) {
      const response = await boardApi.getBoardResponseList(condition, pageable);
      console.log(response);

      this.boardResponseList = response.data._embedded.boardResponseList;
      this.page = response.data.page;
      this.links = response.data._links;
    },
  },
});
</script>

<style scoped></style>
