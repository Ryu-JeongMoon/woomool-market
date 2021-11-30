<template>
  <div>
    <header>회원 정보 페이지</header>
    <v-main>
      <LoadingSpinner v-if="isLoading" />
      <MemberDetailForm
        :submitCallback="moveToMainPage"
        :memberResponse="memberResponse"
      ></MemberDetailForm>
    </v-main>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import LoadingSpinner from "@/components/common/LoadingSpinner.vue";
import MemberDetailForm from "@/views/member/MemberDetailForm.vue";
import { MemberModelResponse } from "@/interfaces/member";
import { LoadingHelper } from "@/utils/loading";
import memberApi from "@/api/MemberApi";
import routerHelper from "@/router/RouterHelper";

export default Vue.extend({
  components: { MemberDetailForm, LoadingSpinner },

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
