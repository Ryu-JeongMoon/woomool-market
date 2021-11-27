<template>
  <v-container>
    <v-form @submit.prevent="submitForm">
      <div>
        <label for="email-input">Email:</label>
        <input
          type="text"
          id="email-input"
          :value="email"
          @input="handleEmailInput"
        />
        <button @click="sendEmailVerification" type="button">
          Request Verification
        </button>
      </div>
      <div>
        <label for="emailAuthString">Email Verification String</label>
        <input type="text" id="emailAuthString" v-model="emailAuthString" />
      </div>
      <div>
        <label for="password-input">Password:</label>
        <input
          type="password"
          id="password-input"
          :value="password"
          @input="handlePasswordInput"
        />
      </div>
      <div>
        <label for="nickname-input">Nickname:</label>
        <input
          type="text"
          id="nickname-input"
          :value="nickname"
          @input="handleNicknameInput"
        />
      </div>
      <div>
        <label for="city-input">City:</label>
        <input
          type="text"
          id="city-input"
          :value="city"
          @input="handleCityInput"
        />
      </div>
      <div>
        <label for="street-input">Street:</label>
        <input
          type="text"
          id="street-input"
          :value="street"
          @input="handleStreetInput"
        />
      </div>
      <div>
        <label for="zipcode-input">Zipcode:</label>
        <input
          type="text"
          id="zipcode-input"
          :value="zipcode"
          @input="handleZipcodeInput"
        />
      </div>

      <div v-if="isVerified">
        <button type="submit">Signup</button>
      </div>
      <div v-else>
        <button type="submit" disabled>Signup</button>
      </div>
      <button @click="verifyEmail" type="button">Verify Email</button>
    </v-form>
  </v-container>
</template>

<script lang="ts">
import Vue from "vue";
import authApi from "@/api/AuthApi";

export default Vue.extend({
  data() {
    return {
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
    handleEmailInput(event: InputEvent) {
      const eventTarget = event.target as HTMLInputElement;
      this.$emit("inputEmail", eventTarget.value);
    },
    handlePasswordInput(event: InputEvent) {
      const eventTarget = event.target as HTMLInputElement;
      this.$emit("inputPassword", eventTarget.value);
    },
    handleNicknameInput(event: InputEvent) {
      const eventTarget = event.target as HTMLInputElement;
      this.$emit("inputNickname", eventTarget.value);
    },
    handleCityInput(event: InputEvent) {
      const eventTarget = event.target as HTMLInputElement;
      this.$emit("inputCity", eventTarget.value);
    },
    handleStreetInput(event: InputEvent) {
      const eventTarget = event.target as HTMLInputElement;
      this.$emit("inputStreet", eventTarget.value);
    },
    handleZipcodeInput(event: InputEvent) {
      const eventTarget = event.target as HTMLInputElement;
      this.$emit("inputZipcode", eventTarget.value);
    },
    submitForm() {
      this.$emit("signup");
    },
    async sendEmailVerification(email: string) {
      await authApi.sendEmailVerification(email);
    },
    async verifyEmail(authString: string) {
      const status = await authApi.verifyEmail(authString);

      if (status === 200) {
        this.isVerified = true;
      }
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
