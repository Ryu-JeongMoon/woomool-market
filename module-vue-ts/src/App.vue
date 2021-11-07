<template>
  <v-app>
    <v-main>
      <AppHeader />
      <router-view />
    </v-main>
  </v-app>
</template>

<script lang="ts">
import Vue from "vue";
import AppHeader from "@/components/common/AppHeader.vue";

export default Vue.extend({
  components: { AppHeader },
});
</script>

<style lang="scss"></style>
