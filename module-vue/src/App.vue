<template>
  <v-app>
    <v-app-bar app>
    </v-app-bar>

    <v-main>
      <h1>{{ title }}</h1>
      <p>{{ count }}</p>
      <button @click="count++">Add</button>
      <router-view></router-view>
    </v-main>
  </v-app>
</template>

<script>
export default {
  el: '#app',
  data() {
    return {
      title: 'Hi',
      count: 1
    }
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
