<template>
  <div>
    <v-text-field
      v-model="props"
      outlined
      dense
      :type="`${type}`"
      :label="`${label}`"
      :rules="rules"
    />
  </div>
</template>

<script lang="ts">
import Vue, { PropType } from "vue";
import { Rule, RULES } from "@/utils/constant/rules";

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
    rules: {
      type: Function as PropType<Rule>,
      required: false,
      default: RULES.EMPTY_RULE,
    },
  },
});
</script>

<style scoped lang="scss"></style>
