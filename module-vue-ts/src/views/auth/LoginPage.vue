<template>
  <div>
    <h1 class="title">Login</h1>
    <div>
      <LoginForm @login="login"></LoginForm>
    </div>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import LoginForm from "@/views/auth/LoginForm.vue";
import { PATH } from "@/router/routes_path";
import { LoginRequest } from "@/interfaces/member";

export default Vue.extend({
  components: { LoginForm },
  methods: {
    async login(loginRequest: LoginRequest) {
      await this.$store
        .dispatch("LOGIN", loginRequest)
        .then(() => this.$router.push(PATH.ROOT));
    },
  },
});
</script>

<style scoped lang="scss"></style>

<!--
this.$router.push('이동할 페이지') 를 이용해 로그인, 회원가입 이후 지정한 페이지로 이동할 수 있다
-->
