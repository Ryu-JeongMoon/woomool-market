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
import AppHeader from "@/components/header/AppHeader.vue";

export default Vue.extend({
  components: { AppHeader },
});
</script>

<style lang="scss">
body {
  text-align: center;
  background-color: #dae1e7;
}
input {
  border-style: groove;
  width: 200px;
}
button {
  border-style: groove;
}
.shadow {
  box-shadow: 5px 10px 10px rgba(0, 0, 0, 0.03);
}
v-file-input {
  border-style: groove;
  width: 200px;
}
</style>
