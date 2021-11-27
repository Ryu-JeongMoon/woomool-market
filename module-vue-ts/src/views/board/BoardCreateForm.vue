<template>
  <div class="contents">
    <h1 class="page-header">Create Page</h1>
    <v-form ref="createBoardForm" class="board-form" @submit.prevent="submit">
      <v-text-field v-model="email" hide-details outlined label="작성자" />
      <v-text-field v-model="title" hide-details outlined label="게시글 제목" />
      <v-select
        v-model="boardCategory"
        :items="boardCategoryNames"
        hide-details
        label="카테고리"
        outlined
        dense
      />
      <v-textarea
        v-model="content"
        label="내용을 작성해주세요"
        hide-details
        outlined
      />

      <v-row>
        <DatetimePicker
          :label="start"
          :setDateTime="setStartDateTime"
        ></DatetimePicker>
        <DatetimePicker
          :label="end"
          :minDatetime="this.startDateTime"
          :setDateTime="setEndDateTime"
        ></DatetimePicker>
      </v-row>

      <v-btn type="submit" color="info">Submit</v-btn>
    </v-form>

    <v-btn class="scroll-to-top mx-2" fab @click="$vuetify.goTo(0, options)">
      <v-icon color="primary">mdi-checkbox-marked-circle</v-icon>
    </v-btn>
  </div>
</template>

<script lang="ts">
import Vue, { PropType } from "vue";
import { BoardRequest } from "@/interfaces/board";
import boardApi from "@/api/BoardApi";
import moment from "moment";
import DatetimePicker from "@/components/common/DatetimePicker.vue";

export default Vue.extend({
  components: { DatetimePicker },

  props: {
    submitClickCallback: {
      type: Function as PropType<(boardRequest: BoardRequest) => Promise<void>>,
      required: true,
    },
  },

  data: () => ({
    start: "시작일시",
    end: "종료일시",
    email: "",
    title: "",
    content: "",
    boardCategory: "",
    startDateTime: moment().format().toString(),
    endDateTime: moment().format().toString(),
    headerTitle: "게시글 작성",
    boardCategoryNames: ["NOTICE", "QNA", "FREE"],
    menu1: false,
    menu2: false,
  }),

  methods: {
    async submit() {
      const boardRequest: BoardRequest = {
        email: this.email,
        title: this.title,
        content: this.content,
        boardCategory: this.boardCategory,
        startDateTime: moment(this.startDateTime).format().substr(0, 19),
        endDateTime: moment(this.endDateTime).format().substr(0, 19),
      };
      await boardApi.createBoard(boardRequest);
    },

    setStartDateTime(date: string, time: string) {
      this.startDateTime = date.concat(" " + time);
    },

    setEndDateTime(date: string, time: string) {
      this.endDateTime = date.concat(" " + time);
    },
  },
});
</script>

<style scoped lang="scss">
.scroll-to-top {
  position: fixed;
  bottom: 20px;
  right: 5vw;
}
</style>
