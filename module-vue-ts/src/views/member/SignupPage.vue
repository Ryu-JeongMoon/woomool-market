<template>
  <div>
    <h1 class="title">Signup</h1>
    <SignupForm
      :email="email"
      :password="password"
      :nickname="nickname"
      :city="city"
      :street="street"
      :zipcode="zipcode"
      @inputEmail="updateEmail"
      @inputPassword="updatePassword"
      @inputNickname="updateNickname"
      @inputCity="updateCity"
      @inputStreet="updateStreet"
      @inputZipcode="updateZipcode"
      @signup="signup"
    />
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import SignupForm from "@/components/member/SignupForm.vue";
import { publicInstance } from "@/api";

export default Vue.extend({
  data() {
    return {
      email: "",
      password: "",
      nickname: "",
      city: "",
      street: "",
      zipcode: "",
    };
  },
  methods: {
    updateEmail(value: string) {
      this.email = value;
    },
    updatePassword(value: string) {
      this.password = value;
    },
    updateNickname(value: string) {
      this.nickname = value;
    },
    updateCity(value: string) {
      this.city = value;
    },
    updateStreet(value: string) {
      this.street = value;
    },
    updateZipcode(value: string) {
      this.zipcode = value;
    },
    signup() {
      const data = {
        email: this.email,
        password: this.password,
        nickname: this.nickname,
        address: {
          city: this.city,
          street: this.street,
          zipcode: this.zipcode,
        },
      };
      publicInstance.post("/api/members", data);
    },
  },
  components: { SignupForm },
});
</script>

<style scoped lang="scss"></style>
