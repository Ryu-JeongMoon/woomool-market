<template>
  <div class="form-container">
    <BoardCreateForm />
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import routerHelper from "@/router/RouterHelper";
import BoardCreateForm from "@/views/board/BoardCreateForm.vue";

export default Vue.extend({
  components: { BoardCreateForm },

  data() {
    return {
      board: "게시판",
    };
  },

  methods: {
    moveToMainPage() {
      routerHelper.moveToMainPage();
    },
  },
});
</script>

<style scoped lang="scss"></style>
