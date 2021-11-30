<template>
  <div>
    <v-text-field
      v-model="props"
      outlined
      dense
      :type="`${type}`"
      :label="`${label}`"
      class="mt-4"
    />
  </div>
</template>

<script lang="ts">
import Vue from "vue";

export default Vue.extend({
  props: {
    props: {
      type: String,
      required: true,
    },
    label: {
      type: String,
      required: true,
    },
    type: {
      type: String,
      required: false,
      default: "text",
    },
    multiple: {
      type: String,
      required: false,
    },
  },
});
</script>

<style scoped lang="scss"></style>
