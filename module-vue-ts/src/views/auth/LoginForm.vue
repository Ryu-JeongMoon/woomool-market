<template>
  <div>
    <v-container>
      <v-form @submit.prevent="login" class="form" lazy-validation>
        <v-text-field type="text" v-model="email" placeholder="email" />
        <br />
        <v-text-field
          type="password"
          v-model="password"
          placeholder="password"
        />
        <br />
        <div>
          <v-btn type="submit">Login <v-icon>login</v-icon></v-btn>
        </div>
        <div>
          <v-btn @click="gotToOauth2Login" class="mt-4"
            >Social Login <v-icon>group</v-icon></v-btn
          >
        </div>
      </v-form>
    </v-container>

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
      email: "",
      password: "",
      showModal: false,
    };
  },

  methods: {
    login() {
      if (this.email === "" || this.password === "") {
        this.showModal = !this.showModal;
      } else {
        this.$emit("login", { email: this.email, password: this.password });
      }
    },
    gotToOauth2Login() {
      this.$router.push("/oauth2");
    },
  },
});
</script>

<style scoped lang="scss">
//input {
//  display: flow;
//  margin: 10px 0;
//  width: 20%;
//  padding: 15px;
//}
//
//button {
//  margin-top: 20px;
//  width: 10%;
//  cursor: pointer;
//}
</style>
