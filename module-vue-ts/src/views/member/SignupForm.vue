<template>
  <div>
    <v-container>
      <v-form @submit.prevent="submit" class="form" lazy-validation>
        <v-text-field
          v-model="email"
          :rules="RULES.MEMBER_EMAIL"
          type="email"
          dense
          outlined
          label="Email"
          class="mt-2"
        />
        <v-btn @click="sendEmailVerification" type="button" outlined
          ><v-icon>verified</v-icon>Verification</v-btn
        >
        <v-text-field
          v-model="emailAuthString"
          :rules="RULES.COMMON_LETTER_NUMBER"
          type="text"
          label="Letters"
          class="mt-6"
          dense
          outlined
        />
        <v-text-field
          v-model="password"
          :rules="RULES.MEMBER_PASSWORD"
          type="password"
          label="Password"
          class="mt-2"
          dense
          outlined
        />
        <v-text-field
          v-model="nickname"
          :rules="RULES.MEMBER_NICKNAME"
          type="text"
          label="Nickname"
          class="mt-2"
          dense
          outlined
        />
        <v-text-field
          v-model="city"
          outlined
          dense
          type="text"
          label="City"
          class="mt-2"
        />
        <v-text-field
          v-model="street"
          type="text"
          label="Street"
          class="mt-2"
          dense
          outlined
        />
        <v-text-field
          v-model="zipcode"
          type="text"
          label="Zipcode"
          class="mt-2"
          dense
          outlined
        />
        <v-btn type="submit" :disabled="!isVerified" class="mt-2 mr-4"
          >Signup</v-btn
        >
        <v-btn
          @click="verifyEmail"
          type="button"
          dense
          color="info"
          class="mt-2"
          ><v-icon>verified</v-icon>Verify Email</v-btn
        >
      </v-form>
    </v-container>
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

<style lang="scss"></style>
