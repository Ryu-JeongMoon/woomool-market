<template>
  <div>
    <header class="page-header">Signup</header>
    <v-main>
      <SignupForm />
    </v-main>
    <ScrollToTop />
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import SignupForm from "@/views/member/SignupForm.vue";
import ScrollToTop from "@/components/button/ScrollToTop.vue";

export default Vue.extend({
  components: { ScrollToTop, SignupForm },
});
</script>

<style scoped lang="scss"></style>

<!--
{ data }로 ES6 destructuring 이용, response.data 를 바로 뽑아낼 수 있다
백틱을 이용해 자바스크립트 변수를 문자열 안에 넣을 수 있당
`${data.email}님 안녕하세요` 요딴식으로다가 사용 가능

function isEmailValid() {
  return validateEmail(this.email);
}
computed 속성을 이용해서 요놈으로 이메일 validation


TODO, signup 이후 페이지 이동이 필요하므로 비동기 처리 후 이동할 수 있게 하장
-->
