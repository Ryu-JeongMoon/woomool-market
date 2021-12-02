<template>
  <div class="contents">
    <h1 class="page-header">Create Page</h1>
    <v-container>
      <v-form ref="form" class="form" @submit.prevent="submit">
        <ReadonlyField :label="'Writer'" :props="email" />
        <v-text-field
          v-model="title"
          outlined
          dense
          :label="'Title'"
          :rules="RULES.BOARD_TITLE"
          class="mt-4"
        />
        <v-select
          v-model="boardCategory"
          :items="boardCategoryNames"
          :rules="RULES.BOARD_CATEGORY"
          label="Category"
          class="mt-4"
          outlined
          dense
        />
        <v-textarea
          v-model="content"
          :rules="RULES.BOARD_CONTENT"
          label="Content"
          class="mt-4"
          outlined
          dense
        />
        <v-row>
          <DatetimePicker :label="start" @setDateTime="setStartDateTime" />
          <DatetimePicker
            :label="end"
            :minDatetime="this.startDateTime"
            @setDateTime="setEndDateTime"
          />
        </v-row>
        <v-col>
          <v-btn type="submit" color="info">
            <v-icon>edit</v-icon>
            Submit
          </v-btn>
        </v-col>
      </v-form>
    </v-container>
  </div>
</template>

<script lang="ts">
import Vue, { PropType } from "vue";
import { BoardRequest } from "@/interfaces/board";
import boardApi from "@/api/BoardApi";
import DatetimePicker from "@/components/common/DatetimePicker.vue";
import { RULES } from "@/utils/constant/rules";
import ReadonlyField from "@/components/common/ReadonlyField.vue";
import { DateUtils } from "@/utils/date";
import routerHelper from "@/router/RouterHelper";
import { WoomoolVueRefs } from "@/types";

export default (Vue as WoomoolVueRefs<{ form: HTMLFormElement }>).extend({
  components: { ReadonlyField, DatetimePicker },

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
      const form = this.$refs.form;
      if (form.validate()) {
        const boardRequest: BoardRequest = {
          email: this.email,
          title: this.title,
          content: this.content,
          boardCategory: this.boardCategory,
          startDateTime: this.startDateTime,
          endDateTime: this.endDateTime,
        };
        await boardApi
          .createBoard(boardRequest)
          .then(routerHelper.goToBoardsPage);
      }
    },

    setStartDateTime(date: string, time: string) {
      this.startDateTime = DateUtils.convertDateFormat(date, time);
    },

    setEndDateTime(date: string, time: string) {
      this.endDateTime = DateUtils.convertDateFormat(date, time);
    },
  },
});
</script>

<style scoped lang="scss"></style>
