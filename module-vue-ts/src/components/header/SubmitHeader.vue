<template>
  <CommonHeader :title="title" @back="$emit('back')">
    <template #option>
      <v-btn text class="header-button" :loading="isLoading" @click="submit">
        {{ submitText }}
      </v-btn>
    </template>
  </CommonHeader>
</template>

<script lang="ts">
import Vue, { PropType } from "vue";
import CommonHeader from "@/components/header/CommonHeader.vue";
export default Vue.extend({
  components: { CommonHeader },

  props: {
    title: String,
    submitCallback: {
      type: Function as PropType<() => any>,
      required: true,
    },
    submitText: {
      type: String,
      default: "완료",
    },
  },

  data() {
    return {
      isLoading: false as boolean,
    };
  },

  methods: {
    submit() {
      const result = this.submitCallback();
      if (result instanceof Promise) {
        result.finally(() => (this.isLoading = false));
      } else {
      }
    },
  },
});
</script>

<style scoped lang="scss">
.header-button {
  color: #292929;
  font-weight: 500;
  font-size: 18px;
  padding-right: 0 !important;
  padding-left: 0 !important;
}
.theme--dark {
  .header-button {
    color: #f5f5f5;
  }
}
</style>
