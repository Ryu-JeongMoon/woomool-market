<template>
  <div>
    <v-container>
      <v-form class="board-form">
        <v-text-field
          v-model="boardResponse.memberResponse.email"
          readonly
          hide-details
          outlined
          filled
          dense
          label="작성자"
        />
        <v-text-field
          v-model="boardResponse.title"
          readonly
          hide-details
          outlined
          filled
          dense
          label="제목"
        />
        <v-textarea
          v-model="boardResponse.content"
          readonly
          hide-details
          outlined
          filled
          dense
          label="내용"
        />
        <v-text-field
          v-model="boardResponse.boardCategory"
          readonly
          hide-details
          outlined
          filled
          dense
          label="카테고리"
        />
        <v-text-field
          v-model="boardResponse.hit"
          readonly
          hide-details
          outlined
          filled
          dense
          label="조회수"
        />
        <v-text-field
          v-model="boardResponse.createdDateTime"
          readonly
          hide-details
          outlined
          filled
          dense
          label="작성일"
        />
      </v-form>
      <v-btn type="button" color="info" @click="goToListPage">Back</v-btn>
    </v-container>
  </div>
</template>

<script lang="ts">
import Vue, { PropType } from "vue";
import { BoardResponse } from "@/interfaces/board";
import { DateUtils } from "@/utils/date";

export default Vue.extend({
  props: {
    boardResponse: {
      type: {} as PropType<BoardResponse>,
      required: true,
    },
  },

  created() {
    this.boardResponse.createdDateTime = DateUtils.getLocalDatetime(
      this.boardResponse.createdDateTime
    );
  },

  methods: {
    goToListPage() {
      this.$router.push("/boards");
    },
  },
});
</script>

<style scoped lang="scss"></style>
