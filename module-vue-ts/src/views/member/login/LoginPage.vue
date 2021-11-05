<template>
  <div>
    <h1>Login</h1>
    <div>
      <LoginForm
        :email="email"
        :password="password"
        @input="updateEmail"
        @inputPw="updatePassword"
        @add="login"
      ></LoginForm>
    </div>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import LoginForm from "@/components/LoginForm.vue";
import { publicInstance } from "@/api";

export default Vue.extend({
  data() {
    return {
      email: "",
      password: "",
    };
  },
  components: { LoginForm },
  methods: {
    updateEmail(value: string) {
      this.email = value;
    },
    login() {
      const data = {
        email: this.email,
        password: this.password,
      };
      publicInstance.post("/api/auth/login", data);
      this.initInput();
    },
    initInput() {
      this.email = "";
      this.password = "";
    },
    updatePassword(value: string) {
      this.password = value;
    },
  },
});
</script>

<style scoped></style>
