<template>
  <div>
    <v-container>
      <v-form class="form">
        <ReadonlyField :label="'Email'" :props="memberResponse.email" />
        <ReadonlyField :label="'Nickname'" :props="memberResponse.nickname" />
        <ReadonlyField :label="'Phone'" :props="memberResponse.phone" />
        <ReadonlyField
          :label="'Profile Image'"
          :props="memberResponse.profileImage"
        />
        <ReadonlyField
          :label="'Signup Date'"
          :props="memberResponse.createdDateTime"
        />
        <ReadonlyField :label="'City'" :props="memberResponse.address.city" />
        <ReadonlyField
          :label="'Address'"
          :props="memberResponse.address.street"
        />
        <ReadonlyField
          :label="'Zipcode'"
          :props="memberResponse.address.zipcode"
        />
        <v-btn @click="submitCallback" color="info" class="mt-4 mr-4">
          <v-icon>arrow_back</v-icon>
          Main
        </v-btn>
        <v-btn @click="goToEditPage" color="warning" class="mt-4">
          <v-icon>edit</v-icon>
          Edit
        </v-btn>
      </v-form>
    </v-container>
  </div>
</template>

<script lang="ts">
import Vue, { PropType } from "vue";
import { MemberModelResponse } from "@/interfaces/member";
import ReadonlyField from "@/components/common/ReadonlyField.vue";
import { WoomoolVueRefs } from "@/types";

export default (Vue as WoomoolVueRefs<{ form: HTMLFormElement }>).extend({
  components: { ReadonlyField },

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

  methods: {
    goToEditPage() {
      this.$router.push({
        name: "ModifyMembers",
        params: { memberId: String(this.memberResponse.id) },
      });
    },
  },
});
</script>

<style scoped lang="scss"></style>
