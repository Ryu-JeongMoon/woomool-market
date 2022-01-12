<template>
  <div>
    <v-container>
      <v-form ref="form" class="form">
        <ReadonlyField :label="'Email'" :props="memberResponse.email" />
        <v-text-field
          v-model="memberModifyRequest.nickname"
          :placeholder="memberResponse.nickname"
          :rules="RULES.MEMBER_EMAIL"
          label="Nickname"
          class="mt-2"
          dense
          outlined
        />
        <v-text-field
          v-model="memberModifyRequest.password"
          :rules="RULES.MEMBER_PASSWORD"
          label="Password"
          type="password"
          class="mt-2"
          dense
          outlined
        />
        <v-text-field
          v-model="memberModifyRequest.phone"
          :placeholder="memberResponse.phone"
          :rules="RULES.MEMBER_PHONE"
          label="Phone"
          class="mt-2"
          dense
          outlined
        />
        <v-file-input
          v-model="image"
          :label="'Profile Image'"
          prepend-icon="upload_file"
          class="mt-2"
          dense
          multiple
          show-size
          outlined
        />
        <ReadonlyField
          :label="'Signup Date'"
          :props="memberResponse.createdDateTime"
        />
        <v-text-field
          v-model="memberModifyRequest.address.city"
          :placeholder="memberResponse.address.city"
          dense
          outlined
          class="mt-2"
          label="City"
        />
        <v-text-field
          v-model="memberModifyRequest.address.street"
          :placeholder="memberResponse.address.street"
          dense
          outlined
          class="mt-2"
          label="Address"
        />
        <v-text-field
          v-model="memberModifyRequest.address.zipcode"
          :placeholder="memberResponse.address.zipcode"
          dense
          outlined
          class="mt-2"
          label="Zipcode"
        />
        <v-btn @click="submitCallback" color="info" class="mt-2 mr-4">
          <v-icon>arrow_back</v-icon>
          Back
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
import { MemberModelResponse, MemberModifyRequest } from "@/interfaces/member";
import ReadonlyField from "@/components/common/ReadonlyField.vue";
import { RULES } from "@/utils/constant/rules";
import { WoomoolVueRefs } from "@/types";
import { Address } from "@/interfaces/common/address";
import { ROUTES_NAME } from "@/router/routes_name";

export default (Vue as WoomoolVueRefs<{ form: HTMLFormElement }>).extend({
  components: { ReadonlyField },

  props: {
    memberResponse: {
      type: {} as PropType<MemberModelResponse>,
      required: true,
    },
    submitCallback: {
      type: Function as PropType<() => void>,
      required: true,
    },
  },

  data: () => ({
    RULES,
    password: "" as string,
    image: File || [],
    memberModifyRequest: {
      nickname: "" as string,
      password: "" as string,
      profileImage: "" as string,
      phone: "" as string,
      license: "" as string,
      address: {
        city: "" as string,
        street: "" as string,
        zipcode: "" as string,
      } as Address,
    } as MemberModifyRequest,
  }),

  methods: {
    async modify() {
      const form = this.$refs.form;
      const modifyRequest: MemberModifyRequest = this.memberModifyRequest;
      if (form.validate()) {
        await this.$store
          .dispatch("REQUEST_MEMBER_MODIFY", modifyRequest)
          .then(() => this.goToDetailPage());
      } else {
        alert("입력이 잘못되었습니다");
      }
    },

    goToDetailPage() {
      this.$router.push({
        name: ROUTES_NAME.MEMBER.DETAIL,
        params: { memberId: String(this.memberResponse.id) },
      });
    },
  },
});
</script>

<style scoped lang="scss"></style>
