<template>
  <v-row class="mx-0 mt-1">
    <v-col pr-3 pl-0>
      <v-dialog
        ref="dateDialog"
        v-model="dateModal"
        :return-value.sync="date"
        :close-on-content-click="true"
        transition="scale-transition"
        offset-y
        persistent
        width="290px"
      >
        <template v-slot:activator="{ on, attrs }">
          <v-text-field
            :value="date + ' ' + time"
            :rules="RULES.DATE"
            :label="`${label}`"
            prepend-icon="event"
            readonly
            v-bind="attrs"
            v-on="on"
          ></v-text-field>
          <small v-show="!isValidDate" class="warning-red"
            >날짜를 선택해주세요</small
          >
        </template>
        <v-date-picker
          v-if="minDatetime != null"
          v-model="date"
          scrollable
          no-title
          :min="`${minDatetime}`"
        >
          <v-spacer></v-spacer>
          <v-btn
            flat
            color="primary"
            @click="
              timeModal = true;
              $refs.dateDialog.save(date);
            "
            >OK
          </v-btn>
          <v-btn flat color="primary" @click="dateModal = false">Cancel</v-btn>
        </v-date-picker>
        <v-date-picker v-else v-model="date" scrollable no-title>
          <v-spacer></v-spacer>
          <v-btn
            flat
            color="primary"
            @click="
              timeModal = true;
              $refs.dateDialog.save(date);
            "
            >OK
          </v-btn>
          <v-btn flat color="primary" @click="dateModal = false">Cancel</v-btn>
        </v-date-picker>
      </v-dialog>
    </v-col>
    <v-col class="px-0" cols="12">
      <v-dialog
        ref="timeDialog"
        v-model="timeModal"
        :return-value.sync="time"
        :close-on-content-click="false"
        transition="scale-transition"
        offset-y
        persistent
        width="290px"
      >
        <v-time-picker v-if="timeModal" v-model="time" scrollable no-title>
          <v-spacer></v-spacer>
          <v-btn
            text
            color="primary"
            @click="
              $refs.timeDialog.save(time);
              $emit('setDateTime', date, time);
            "
            >OK
          </v-btn>
          <v-btn text color="primary" @click="timeModal = false">Cancel</v-btn>
        </v-time-picker>
      </v-dialog>
    </v-col>
  </v-row>
</template>

<script lang="ts">
import Vue from "vue";
import { RULES } from "@/utils/constant/rules";

export default Vue.extend({
  props: {
    label: {
      type: String,
      required: true,
    },
    minDatetime: {
      type: String,
      required: false,
    },
  },

  data: () => ({
    RULES,
    date: "" as string,
    dateModal: false,
    time: "" as string,
    timeModal: false,
  }),

  computed: {
    isValidDate(): boolean {
      return this.date != "" && this.time != "";
    },
  },
});
</script>

<style lang="scss">
.warning-red {
  color: red;
}
</style>
