<template>
  <header>
    <div>
      <template>
        <router-link to="/">
          <v-icon>home</v-icon>
        </router-link>
        |
        <router-link to="/about">
          <v-icon>info</v-icon>
        </router-link>
        |

        <router-link to="#">
          <v-icon>light_mode</v-icon>
        </router-link>
        |
        <router-link to="#">
          <v-icon>bedtime</v-icon>
        </router-link>
      </template>
    </div>
    <div class="navigations">
      <template v-if="isLogin">
        <router-link to="/chat">Chat <v-icon>message</v-icon></router-link> |
        <a @click="goToMemberDetailPage">{{ $store.state.member.username }} </a>
        <v-icon>account_circle</v-icon> |
        <a href="#" @click="logout">Logout <v-icon>logout</v-icon></a> |
      </template>
      <template v-else>
        <router-link to="/login"
          >Login
          <v-icon>login</v-icon>
        </router-link>
        |
        <router-link to="/signup"
          >Signup
          <v-icon>3p</v-icon>
        </router-link>
      </template>
    </div>
  </header>
</template>

<script lang="ts">
import Vue from "vue";
import memberApi from "@/api/MemberApi";
import { ROUTES_NAME } from "@/router/routes_name";

export default Vue.extend({
  computed: {
    isLogin() {
      return this.$store.getters.isLogin;
    },
  },

  methods: {
    logout() {
      this.$store.dispatch("LOGOUT");
      this.$router.push(ROUTES_NAME.AUTH.LOGIN);
    },

    async goToMemberDetailPage() {
      await memberApi
        .getIdBy(this.$store.state.member.username)
        .then((memberId) => {
          this.$router.push({
            name: ROUTES_NAME.MEMBER.DETAIL,
            params: { memberId: memberId.toString() },
          });
        });
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
