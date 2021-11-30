<template>
  <header>
    <div>
      <router-link to="/"><v-icon>home</v-icon></router-link> |
      <router-link to="/about">About</router-link>
    </div>
    <div class="navigations">
      <template v-if="isLogin">
        <span>{{ $store.state.member.username }}</span> |
        <a href="#" @click="logout">Logout</a>
      </template>
      <template v-else>
        <router-link to="/login">Login<v-icon>login</v-icon></router-link> |
        <router-link to="/signup">Signup<v-icon>3p</v-icon></router-link> |
        <router-link to="/chat">Chat<v-icon>message</v-icon></router-link>
      </template>
      <template>
        <router-link to="#">Day<v-icon>light_mode</v-icon></router-link>
        <router-link to="#">Night<v-icon>bedtime</v-icon></router-link>
      </template>
    </div>
  </header>
</template>

<script lang="ts">
import Vue from "vue";

export default Vue.extend({
  computed: {
    isLogin() {
      return this.$store.getters.isLogin;
    },
  },
  methods: {
    logout() {
      this.$store.dispatch("LOGOUT");
      this.$router.push("/login");
    },
  },
});
</script>

<style scoped lang="scss">
header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  background-color: #927dfc;
  z-index: 2;
  box-shadow: 0px 3px 10px rgba(0, 0, 0, 0.05);
}
.fixed {
  position: fixed;
  top: 0;
  width: 100%;
}
</style>

<!--
<span>{{ $store.state.username }}</span>
요래 시도하다가 안 나왔당
store/type/types.ts 가서 보면 하위로 member... 등등 가지고 있으니께 나눠서 저장된 것

<span>{{ $store.state.member.username }}</span>
요래 꺼내야한다

day, night 으로 다크 테마 적용하기
-->
