<template>
  <div>
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
              v-for="boardResponse in boardResponseList"
              v-bind:key="boardResponse.id"
            >
              <td>{{ boardResponse.id }}</td>
              <td @click="goToDetailBoard(boardResponse.id)">
                {{ boardResponse.title }}
              </td>
              <td>{{ boardResponse.memberResponse.email }}</td>
              <td>{{ boardResponse.hit }}</td>
              <td>{{ boardResponse.boardCategory }}</td>
              <td>{{ getLocalDate(boardResponse.createdDateTime) }}</td>
            </tr>
          </tbody>
        </v-simple-table>
        <span v-for="page in page.totalPages" v-bind:key="page">
          <td>
            <button>{{ page }}</button>
          </td>
        </span>
      </v-container>
    </div>
  </div>
</template>

<script lang="ts">
import Vue, { PropType } from "vue";
import { BoardResponse } from "@/interfaces/board";
import { Page } from "@/interfaces/common/page";
import { Links } from "@/interfaces/common/links";
import { DateUtils } from "@/utils/date";

export default Vue.extend({
  props: {
    boardResponseList: {
      type: [] as PropType<BoardResponse[]>,
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
