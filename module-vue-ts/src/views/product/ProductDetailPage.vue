<template>
  <div>
    <h1>{{ productId }}</h1>
  </div>
</template>

<script lang="ts">
import Vue from "vue";

export default Vue.extend({
  props: {
    productId: {
      type: Number,
      required: true,
    },
  },
});
</script>

<style scoped lang="scss"></style>
