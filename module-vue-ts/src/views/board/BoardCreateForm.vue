<template>
  <div class="contents">
    <h1 class="page-header">Create Page</h1>
    <v-form ref="createBoardForm" class="form" @submit.prevent="submit">
      <v-row>
        <v-col cols="12">
          <v-text-field
            v-model="email"
            disabled
            outlined
            filled
            label="Writer"
            class="mt-4"
          />
        </v-col>
        <v-col cols="12">
          <v-text-field
            v-model="title"
            :rules="RULES.BOARD_TITLE"
            outlined
            label="Title"
            class="mt-4"
          />
        </v-col>
        <v-col cols="12">
          <v-select
            v-model="boardCategory"
            :items="boardCategoryNames"
            :rules="RULES.BOARD_CATEGORY"
            label="Category"
            class="mt-4"
            outlined
            dense
          />
        </v-col>
        <v-col cols="12">
          <v-textarea
            v-model="content"
            :rules="RULES.BOARD_CONTENT"
            label="Content"
            class="mt-4"
            outlined
          />
        </v-col>
        <v-col cols="12">
          <v-row>
            <DatetimePicker
              :label="start"
              @setDateTime="setStartDateTime"
            ></DatetimePicker>
            <DatetimePicker
              :label="end"
              :minDatetime="this.startDateTime"
              @setDateTime="setEndDateTime"
            ></DatetimePicker>
          </v-row>
        </v-col>
        <v-col>
          <v-btn type="submit" color="info">Submit</v-btn>
        </v-col>
      </v-row>
    </v-form>
  </div>
</template>

<script lang="ts">
import Vue, { PropType } from "vue";
import { BoardRequest } from "@/interfaces/board";
import boardApi from "@/api/BoardApi";
import moment from "moment";
import DatetimePicker from "@/components/common/DatetimePicker.vue";
import { RULES } from "@/utils/constant/rules";

export default Vue.extend({
  components: { DatetimePicker },

  props: {
    submitClickCallback: {
      type: Function as PropType<(boardRequest: BoardRequest) => Promise<void>>,
      required: true,
    },
  },

  data: () => ({
    RULES,
    start: "시작일시",
    end: "종료일시",
    email: "",
    title: "",
    content: "",
    boardCategory: "",
    startDateTime: "",
    endDateTime: "",
    headerTitle: "게시글 작성",
    boardCategoryNames: ["NOTICE", "QNA", "FREE"],
    menu1: false,
    menu2: false,
  }),

  created() {
    this.email = this.$store.state.member.username;
  },

  methods: {
    async submit() {
      const createBoardForm = this.$refs.createBoardForm as HTMLFormElement;
      if (createBoardForm.validate()) {
        const boardRequest: BoardRequest = {
          email: this.email,
          title: this.title,
          content: this.content,
          boardCategory: this.boardCategory,
          startDateTime: moment(this.startDateTime).format().substr(0, 19),
          endDateTime: moment(this.endDateTime).format().substr(0, 19),
        };
        await boardApi.createBoard(boardRequest);
      }
    },

    setStartDateTime(date: string, time: string) {
      console.log("panda");
      this.startDateTime = date.concat(" " + time);
    },

    setEndDateTime(date: string, time: string) {
      console.log("bear");
      this.endDateTime = date.concat(" " + time);
    },
  },
});
</script>

<style scoped lang="scss">
.v-text-field {
  margin: 12px 0;
}
</style>
