<template>
  <div class="contents">
    <h1 class="page-header">Signup</h1>
    <v-form @submit.prevent="submit" class="form" lazy-validation>
      <v-text-field
        v-model="email"
        :rules="RULES.MEMBER_EMAIL"
        type="email"
        outlined
        label="Email"
        class="mt-4"
      />
      <v-btn
        block
        @click="sendEmailVerification"
        type="button"
        class="mt-4"
        outlined
        ><v-icon>verified</v-icon>Verification</v-btn
      >
      <v-text-field
        v-model="emailAuthString"
        outlined
        type="text"
        label="Letters"
        class="mt-4"
      />
      <v-text-field
        v-model="password"
        :rules="RULES.MEMBER_PASSWORD"
        outlined
        type="password"
        label="Password"
        class="mt-4"
      />
      <v-text-field
        v-model="nickname"
        :rules="RULES.MEMBER_NICKNAME"
        outlined
        type="text"
        label="Nickname"
        class="mt-4"
      />
      <v-text-field
        v-model="city"
        outlined
        type="text"
        label="City"
        class="mt-4"
      />
      <v-text-field
        v-model="street"
        outlined
        type="text"
        label="Street"
        class="mt-4"
      />
      <v-text-field
        v-model="zipcode"
        outlined
        type="text"
        label="Zipcode"
        class="mt-4"
      />

      <v-row>
        <div>
          <v-btn type="submit" :disabled="!isVerified">Signup</v-btn>
        </div>
        <div>
          <v-btn @click="verifyEmail" type="button"><v-icon></v-icon></v-btn>
          <small>Verify Email</small>
        </div>
      </v-row>
    </v-form>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import authApi from "@/api/AuthApi";
import { RULES } from "@/utils/constant/rules";

export default Vue.extend({
  data() {
    return {
      RULES,
      emailAuthString: "",
      isVerified: false,
      email: "",
      password: "",
      nickname: "",
      city: "",
      street: "",
      zipcode: "",
    };
  },

  methods: {
    submit() {
      this.$emit("signup");
    },
    async sendEmailVerification(email: string) {
      await authApi.sendEmailVerification(email);
    },
    async verifyEmail(authString: string) {
      await authApi.verifyEmail(authString).then((status) => {
        if (status === 200) this.isVerified = true;
      });
    },
  },
});
</script>

<style>
label {
  display: inline-block;
  margin-top: 20px;
}

input {
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

<!--
한글 사용 시 validation 엄격하게 하기 위해 props 이용했는데 넘 더러움
그냥 v-model 로 엮는게 나을듯
-->
