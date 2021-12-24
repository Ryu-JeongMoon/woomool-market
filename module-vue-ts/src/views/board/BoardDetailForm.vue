<template>
  <div>
    <v-container>
      <v-form class="form">
        <ReadonlyField
          :label="'Writer'"
          :props="boardResponse.memberResponse.email"
        />
        <ReadonlyField
          :label="'Title'"
          :props="boardResponse.createdDateTime"
        />
        <ReadonlyField :label="'Content'" :props="boardResponse.title" />
        <ReadonlyField
          :label="'Category'"
          :props="boardResponse.createdDateTime"
        />
        <ReadonlyField :label="'Hit'" :props="boardResponse.hit.toString()" />
        <ReadonlyField :label="'Date'" :props="boardResponse.createdDateTime" />
        <v-btn @click="submitCallback" color="info" class="mt-4">
          <v-icon>arrow_back</v-icon>
          Back
        </v-btn>
      </v-form>
    </v-container>
  </div>
</template>

<script lang="ts">
import Vue, { PropType } from "vue";
import { BoardResponse } from "@/interfaces/board";
import { DateUtils } from "@/utils/date";
import ReadonlyField from "@/components/common/ReadonlyField.vue";

export default Vue.extend({
  components: { ReadonlyField },

  props: {
    boardResponse: {
      type: {} as PropType<BoardResponse>,
      required: true,
    },
    submitCallback: {
      type: Function as PropType<() => Promise<void>>,
      required: true,
    },
  },

  created() {
    this.boardResponse.createdDateTime = DateUtils.getLocalDatetime(
      this.boardResponse.createdDateTime
    );
  },
});
</script>

<style scoped lang="scss"></style>
