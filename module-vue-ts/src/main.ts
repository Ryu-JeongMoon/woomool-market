import Vue from "vue";
import App from "./App.vue";
import store from "./store";
import router from "./router";
import { vuetify } from "@/plugins/vuetify";

import "@/fonts/roboto.css";
import "@/fonts/fonts.css";

Vue.config.productionTip = false;

new Vue({
  vuetify,
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");
