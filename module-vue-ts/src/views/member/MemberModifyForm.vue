<template>
  <div>
    <v-container>
      <v-form ref="form" class="form">
        <ReadonlyField :label="'Email'" :props="memberResponse.email" />
        <TextField
          :label="'Nickname'"
          :props="memberResponse.nickname"
          :rules="RULES.MEMBER_EMAIL"
        />
        <TextField
          :label="'Password'"
          :props="password"
          :type="'password'"
          :rules="RULES.MEMBER_PASSWORD"
        />
        <TextField
          :label="'Phone'"
          :props="memberResponse.phone"
          :rules="RULES.MEMBER_PHONE"
        />
        <VFileInput
          prepend-icon="upload_file"
          multiple
          v-model="image"
          show-size
          :label="'Profile Image'"
          outlined
          class="mt-2"
        ></VFileInput>
        <ReadonlyField
          :label="'Signup Date'"
          :props="memberResponse.createdDateTime"
        />
        <TextField :label="'City'" :props="memberResponse.address.city" />
        <TextField :label="'Address'" :props="memberResponse.address.street" />
        <TextField :label="'Zipcode'" :props="memberResponse.address.zipcode" />
        <v-btn @click="submitCallback" color="info" class="mt-2 mr-4">
          <v-icon>arrow_back</v-icon>
          Main
        </v-btn>
        <v-btn @click="modify" color="green" class="mt-2">
          <v-icon>send</v-icon>
          Submit
        </v-btn>
      </v-form>
    </v-container>
  </div>
</template>

<script lang="ts">
import Vue, { PropType } from "vue";
import TextField from "@/components/common/TextField.vue";
import { MemberModelResponse, MemberModifyRequest } from "@/interfaces/member";
import ReadonlyField from "@/components/common/ReadonlyField.vue";
import { RULES } from "@/utils/constant/rules";
import { WoomoolVueRefs } from "@/types";

export default (Vue as WoomoolVueRefs<{ form: HTMLFormElement }>).extend({
  components: { ReadonlyField, TextField },

  props: {
    memberResponse: {
      type: {} as PropType<MemberModelResponse>,
      required: true,
    },
    submitCallback: {
      type: Function as PropType<() => Promise<void>>,
      required: true,
    },
  },

  data: () => ({
    RULES,
    password: "" as string,
    image: File,
  }),

  methods: {
    async modify() {
      const form = this.$refs.form;

      const modifyRequest: MemberModifyRequest = {
        nickname: this.memberResponse.nickname,
        password: this.password,
        profileImage: this.memberResponse.profileImage,
        phone: this.memberResponse.phone,
        license: this.memberResponse.license,
        address: {
          city: this.memberResponse.address.city,
          street: this.memberResponse.address.street,
          zipcode: this.memberResponse.address.zipcode,
        },
      };

      if (form.validate()) {
        await this.$store.dispatch("REQUEST_MEMBER_MODIFY", modifyRequest);
      }
    },
  },
});
</script>

<style scoped lang="scss"></style>
