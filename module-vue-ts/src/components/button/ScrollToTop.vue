<template>
  <div>
    <v-btn class="scroll-to-top mx-2" fab @click="$vuetify.goTo(0, options)">
      <v-icon color="primary" dark>arrow_upward</v-icon>
    </v-btn>
  </div>
</template>

<script lang="ts">
import Vue from "vue";

export default Vue.extend({});
</script>

<style scoped lang="scss">
.scroll-to-top {
  position: fixed;
  bottom: 20px;
  right: 5vw;
}
</style>
