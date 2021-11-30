<template>
  <div>
    <header>회원 정보 페이지</header>
    <v-main>
      <LoadingSpinner v-if="isLoading" />
      <MemberModifyForm
        :submitCallback="moveToMainPage"
        :memberResponse="memberResponse"
      ></MemberModifyForm>
    </v-main>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import MemberModifyForm from "@/views/member/MemberModifyForm.vue";
import { MemberModelResponse } from "@/interfaces/member";
import { LoadingHelper } from "@/utils/loading";
import memberApi from "@/api/MemberApi";
import routerHelper from "@/router/RouterHelper";

export default Vue.extend({
  components: { MemberModifyForm },

  data() {
    return {
      isLoading: false,
      memberResponse: {} as MemberModelResponse,
    };
  },

  props: {
    memberId: {
      type: Number,
      required: true,
    },
  },

  created() {
    this.fetchMemberResponse();
  },

  methods: {
    async fetchMemberResponse() {
      LoadingHelper.switchLoadingState(this.isLoading);
      this.memberResponse = await memberApi
        .getMemberModelResponse(this.memberId)
        .finally(() => LoadingHelper.switchLoadingState(this.isLoading));
    },

    moveToMainPage() {
      routerHelper.goToMainPage();
    },
  },
});
</script>

<style scoped lang="scss"></style>

<!--
회원 상세 페이지 갈때랑 똑같다?!
-->
