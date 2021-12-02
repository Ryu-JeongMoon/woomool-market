<template>
  <div>
    <v-text-field
      v-model="props"
      readonly
      outlined
      filled
      dense
      :label="`${label}`"
      class="mt-2"
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
  },
});
</script>

<style scoped lang="scss"></style>
