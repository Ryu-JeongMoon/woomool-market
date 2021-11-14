<template>
  <div>
    <div class="row">
      <div class="col-md-10">
        <a
          href="https://localhost:8443/oauth2/authorization/facebook"
          class="btn_social btn-secondary active"
          role="button"
          >Facebook Login</a
        ><br />
        <a
          href="https://localhost:8443/oauth2/authorization/google"
          class="btn_social btn-secondary active"
          role="button"
          >Google Login</a
        ><br />
        <a
          href="https://localhost:8443/oauth2/authorization/kakao"
          class="btn_social btn-secondary active"
          role="button"
          >Kakao Login</a
        ><br />
        <a
          href="https://localhost:8443/oauth2/authorization/naver"
          class="btn_social btn-secondary active"
          role="button"
          >Naver Login</a
        ><br />
        <a
          href="https://localhost:8443/oauth2/authorization/github"
          class="btn_social btn-secondary active"
          role="button"
          >GitHub Login</a
        ><br />
      </div>
      <div id="disqus_thread"></div>
    </div>
  </div>
</template>

<script lang="ts">
import Vue from "vue";

export default Vue.extend({});
</script>

<style scoped></style>
