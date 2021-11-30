<template>
  <div>
    <v-container>
      <v-form ref="form" class="form">
        <ReadonlyField :label="'Email'" :props="memberResponse.email" />
        <TextField :label="'Nickname'" :props="memberResponse.nickname" />
        <TextField :label="'Password'" :props="password" :type="'password'" />
        <TextField :label="'Phone'" :props="memberResponse.phone" />
        <VFileInput
          align="center"
          prepend-icon="upload_file"
          multiple
          v-model="image"
          show-size
          :label="'Profile Image'"
          dense
          outlined
          class="mt-4 mr-0 ml-0"
        ></VFileInput>
        <ReadonlyField
          :label="'Signup Date'"
          :props="memberResponse.createdDateTime"
        />
        <TextField :label="'City'" :props="memberResponse.address.city" />
        <TextField :label="'Address'" :props="memberResponse.address.street" />
        <TextField :label="'Zipcode'" :props="memberResponse.address.zipcode" />
        <v-btn @click="submitCallback" color="info" class="mt-4 mr-4">
          <v-icon>arrow_back</v-icon>
          Main
        </v-btn>
        <v-btn @click="modify" color="green" class="mt-4">
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
import { Address } from "@/interfaces/common/address";

export default Vue.extend({
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
    password: "" as string,
    image: File,
  }),

  methods: {
    async modify() {
      const form = this.$refs.form as HTMLFormElement;

      const modifyRequest: MemberModifyRequest = {
        nickname: this.memberResponse.nickname,
        password: this.password,
        profileImage: this.memberResponse.profileImage,
        phone: this.memberResponse.phone,
        license: this.memberResponse.license,
        address: this.memberResponse.address,
      };

      if (form.validate()) {
        await this.$store.dispatch("REQUEST_MEMBER_MODIFY", modifyRequest);
      }
    },
  },
});
</script>

<style scoped lang="scss"></style>
