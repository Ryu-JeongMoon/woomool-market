<template>
  <div>
    <h1 class="title">Login</h1>
    <div>
      <LoginForm
        :email="email"
        :password="password"
        @inputEmail="updateEmail"
        @inputPw="updatePassword"
        @login="login"
      ></LoginForm>
    </div>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import LoginForm from "@/views/member/LoginForm.vue";
import { publicAxios } from "@/api";

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
      publicAxios.post("/api/auth/login", data);
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

<style scoped lang="scss"></style>
