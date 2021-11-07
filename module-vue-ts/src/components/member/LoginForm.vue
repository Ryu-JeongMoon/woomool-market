<template>
  <div>
    <div>
      <input
        type="text"
        :value="email"
        placeholder="email"
        @input="handleEmailInput"
      />
      <br />
      <input
        type="password"
        :value="password"
        placeholder="password"
        @input="handlePasswordInput"
      />
      <br />
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
      this.$emit("inputEmail", eventTarget.value);
    },
    handlePasswordInput(event: InputEvent) {
      const eventTarget = event.target as HTMLInputElement;
      this.$emit("inputPw", eventTarget.value);
    },
    login() {
      this.$emit("login");
    },
    oauth2Login() {
      this.$router.push("/oauth2");
    },
  },
});
</script>

<style scoped lang="scss">
input {
  display: flow;
  margin: 10px 0;
  width: 20%;
  padding: 15px;
}
button {
  margin-top: 20px;
  width: 10%;
  cursor: pointer;
}
</style>
