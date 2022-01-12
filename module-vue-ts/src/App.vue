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
input {
  width: 200px;
}

.shadow {
  box-shadow: 5px 10px 10px rgba(0, 0, 0, 0.03);
}

v-file-input {
  border-style: groove;
  width: 200px;
}

a {
  color: #364f6b;
  text-decoration: none;
}

a:hover {
  color: #3fc1c9;
}

ul {
  list-style: none;
}

li {
  list-style: none;
}
</style>
