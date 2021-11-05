<template>
  <div id="app">
    <div id="nav">
      <router-link to="/">Home</router-link>
      |
      <router-link to="/about">About</router-link>
      |
      <router-link to="/login">Login</router-link>
      |
      <router-link to="/signup">Signup</router-link>
    </div>
    <router-view />
  </div>
</template>

<script lang="ts">
import Vue from "vue";

export default Vue.extend({});
</script>

<style>
@import "./css/reset.css";
@import "./css/common.css";
</style>
