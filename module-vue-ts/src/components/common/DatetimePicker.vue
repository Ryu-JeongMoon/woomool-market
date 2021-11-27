<template>
  <v-col cols="4" sm="6" md="4">
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
          v-model="date"
          :label="`${label}`"
          prepend-icon="event"
          readonly
          v-bind="attrs"
          v-on="on"
        ></v-text-field>
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
          >OK</v-btn
        >
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
          >OK</v-btn
        >
        <v-btn flat color="primary" @click="dateModal = false">Cancel</v-btn>
      </v-date-picker>
    </v-dialog>

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
        <v-btn text color="primary" @click="$refs.timeDialog.save(time)"
          >OK</v-btn
        >
        <v-btn text color="primary" @click="timeModal = false">Cancel</v-btn>
      </v-time-picker>
    </v-dialog>
  </v-col>
</template>

<script lang="ts">
import Vue from "vue";

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
    date: "" as string,
    dateModal: false,
    time: "" as string,
    timeModal: false,
  }),

  methods: {
    // set: () => {
    //   this.date = this.date + " " + this.time;
    //   this.$refs.dateDialog.save(this.date);
    //   this.$refs.timeDialog.save(this.time);
    //   this.$emit("setDatetime", { date: this.date, time: this.time });
    // },
  },
});
</script>
