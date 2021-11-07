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
      <button type="submit">Signup</button>
    </v-form>
  </v-container>
</template>

<script lang="ts">
import Vue from "vue";

export default Vue.extend({
  props: {
    email: {
      type: String,
      required: true,
    },
    password: {
      type: String,
      required: true,
    },
    nickname: {
      type: String,
      required: true,
    },
    city: {
      type: String,
      required: false,
    },
    street: {
      type: String,
      required: false,
    },
    zipcode: {
      type: String,
      required: false,
    },
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
