<template>
  <div>
    <div>
      <input
        type="text"
        :value="email"
        placeholder="email"
        @input="handleEmailInput"
      />
      <br />
      <input
        type="password"
        :value="password"
        placeholder="password"
        @input="handlePasswordInput"
      />
      <br />
      <button @click="login">Login</button>
    </div>
    <div>
      <button @click="oauth2Login">Social Login</button>
    </div>
    <Modal v-if="showModal" @close="showModal = false">
      <h3 slot="header">
        삐빅
        <v-icon>$delete</v-icon>
      </h3>
      <h3 slot="body">빈 값을 입력할 수 없습니다</h3>
    </Modal>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import Modal from "@/components/common/Modal.vue";

export default Vue.extend({
  components: { Modal },

  data() {
    return {
      showModal: false,
    };
  },

  props: {
    email: {
      type: String,
      required: true,
    },
    password: {
      type: String,
      required: true,
    },
  },

  methods: {
    handleEmailInput(event: InputEvent) {
      const eventTarget = event.target as HTMLInputElement;
      this.$emit("inputEmail", eventTarget.value);
    },
    handlePasswordInput(event: InputEvent) {
      const eventTarget = event.target as HTMLInputElement;
      this.$emit("inputPw", eventTarget.value);
    },
    login() {
      if (this.email == "" || this.password == "") {
        this.showModal = !this.showModal;
      } else {
        this.$emit("login");
      }
    },
    oauth2Login() {
      this.$router.push("/oauth2");
    },
  },
});
</script>

<style scoped lang="scss">
input {
  display: flow;
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
