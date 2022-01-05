<template>
  <div>
    <v-container>
      <v-simple-table>
        <thead>
          <th>NO.</th>
          <th>TITLE.</th>
          <th>WRITER.</th>
          <th>HIT.</th>
          <th>CATEGORY.</th>
          <th>DATE.</th>
        </thead>
        <tbody>
          <tr
            v-for="boardQueryResponse in boardQueryResponseList"
            v-bind:key="boardQueryResponse.id"
          >
            <td>{{ boardQueryResponse.id }}</td>
            <v-hover
              v-slot:default="{ hover }"
              open-delay="100"
              class="slide-enter-active slide-leave-active"
            >
              <td
                @click="goToDetailBoard(boardQueryResponse.id)"
                class="ui-state-hover"
                :elevation="hover ? 12 : 2"
              >
                <a>{{ boardQueryResponse.title }}</a>
              </td>
            </v-hover>
            <td>{{ boardQueryResponse.memberResponse.email }}</td>
            <td>{{ boardQueryResponse.hit }}</td>
            <td>{{ boardQueryResponse.boardCategory }}</td>
            <td>{{ getLocalDate(boardQueryResponse.createdDateTime) }}</td>
          </tr>
        </tbody>
      </v-simple-table>
      <span v-for="page in page.totalPages" v-bind:key="page">
        <td>
          <v-btn
            small
            rounded
            color="info"
            @click="goToSpecificPage(page)"
            class="mr-1 mt-4"
          >
            {{ page }}
          </v-btn>
        </td>
      </span>
    </v-container>
  </div>
</template>

<script lang="ts">
import Vue, { PropType } from "vue";
import { BoardQueryResponse } from "@/interfaces/board";
import { Page } from "@/interfaces/common/page";
import { Links } from "@/interfaces/common/links";
import { DateUtils } from "@/utils/date";

export default Vue.extend({
  props: {
    boardQueryResponseList: {
      type: [] as PropType<BoardQueryResponse[]>,
      required: true,
    },
    page: {
      type: {} as PropType<Page>,
      required: true,
    },
    links: {
      type: {} as PropType<Links>,
      required: false,
    },
  },

  methods: {
    getLocalDate(localDateTime: string) {
      return DateUtils.getLocalDatetime(localDateTime);
    },

    goToDetailBoard(boardId: number) {
      this.$router.push(`/boards/${boardId}`);
    },

    goToSpecificPage(page: number) {
      const pageable = {
        page: page - 1,
        size: 10,
      };
      this.$emit("movePage", pageable);
      // this.$router.push(`/boards?pageable=${pageable}`);
    },
  },
});
</script>

<style scoped lang="scss">
.slide-leave-active,
.slide-enter-active {
  transition: 1s;
}

.slide-enter {
  transform: translate(0, 100%);
}

.slide-leave-to {
  transform: translate(0, -100%);
}
</style>

<!--
v-data-table 로 추후 변경할 것
-->
