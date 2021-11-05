<template>
  <div>
    <div>
      <label for="email-input">Email</label>
      <input
        type="text"
        id="email-input"
        :value="email"
        @input="handleEmailInput"
      />
      <label for="password-input">Password</label>
      <input
        type="text"
        id="password-input"
        :value="password"
        @input="handlePasswordInput"
      />
      <button @click="login">Login</button>
    </div>
    <div>
      <button @click="oauth2Login">Social Login</button>
    </div>
  </div>
</template>

<script lang="ts">
import Vue from "vue";

export default Vue.extend({
  props: {
    email: {
      type: String,
      required: true,
    },
    password: {
      type: String,
      required: true,
    },
  },
  methods: {
    handleEmailInput(event: InputEvent) {
      const eventTarget = event.target as HTMLInputElement;
      this.$emit("input", eventTarget.value);
    },
    handlePasswordInput(event: InputEvent) {
      const eventTarget = event.target as HTMLInputElement;
      this.$emit("inputPw", eventTarget.value);
    },
    login() {
      this.$emit("add");
      console.log("hello");
    },
    oauth2Login() {
      this.$router.push("/oauth2");
    },
  },
});
</script>

<style scoped></style>
